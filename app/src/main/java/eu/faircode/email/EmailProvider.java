package eu.faircode.email;

/*
    This file is part of FairEmail.

    FairEmail is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    FairEmail is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with FairEmail.  If not, see <http://www.gnu.org/licenses/>.

    Copyright 2018-2019 by Marcel Bokhorst (M66B)
*/

import android.content.Context;
import android.content.res.XmlResourceParser;

import org.xbill.DNS.Lookup;
import org.xbill.DNS.Record;
import org.xbill.DNS.SRVRecord;
import org.xbill.DNS.SimpleResolver;
import org.xbill.DNS.TextParseException;
import org.xbill.DNS.Type;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.URL;
import java.net.UnknownHostException;
import java.text.Collator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class EmailProvider {
    public String name;
    public int order;
    public int keepalive;
    public String link;
    public String type;
    public String imap_host;
    public boolean imap_starttls;
    public int imap_port;
    public String smtp_host;
    public int smtp_port;
    public boolean smtp_starttls;
    public UserType user = UserType.EMAIL;
    public String helpUrl = null;
    public StringBuilder documentation = null; // html

    enum UserType {LOCAL, EMAIL}

    private static final int DNS_TIMEOUT = 5 * 1000; // milliseconds
    private static final int ISPDB_TIMEOUT = 20 * 1000; // milliseconds

    private EmailProvider() {
    }

    EmailProvider(String name) {
        this.name = name;
    }

    private void checkValid() throws UnknownHostException {
        if (this.imap_host == null || this.imap_port == 0 ||
                this.smtp_host == null || this.smtp_port == 0)
            throw new UnknownHostException(this.name + " invalid");
    }

    static List<EmailProvider> loadProfiles(Context context) {
        List<EmailProvider> result = null;
        try {
            EmailProvider provider = null;
            XmlResourceParser xml = context.getResources().getXml(R.xml.providers);
            int eventType = xml.getEventType();
            while (eventType != XmlPullParser.END_DOCUMENT) {
                if (eventType == XmlPullParser.START_TAG) {
                    String name = xml.getName();
                    if ("providers".equals(name))
                        result = new ArrayList<>();
                    else if ("provider".equals(name)) {
                        provider = new EmailProvider();
                        provider.name = xml.getAttributeValue(null, "name");
                        provider.order = xml.getAttributeIntValue(null, "order", Integer.MAX_VALUE);
                        provider.keepalive = xml.getAttributeIntValue(null, "keepalive", 0);
                        provider.link = xml.getAttributeValue(null, "link");
                        provider.type = xml.getAttributeValue(null, "type");
                    } else if ("imap".equals(name)) {
                        provider.imap_host = xml.getAttributeValue(null, "host");
                        provider.imap_port = xml.getAttributeIntValue(null, "port", 0);
                        provider.imap_starttls = xml.getAttributeBooleanValue(null, "starttls", false);
                    } else if ("smtp".equals(name)) {
                        provider.smtp_host = xml.getAttributeValue(null, "host");
                        provider.smtp_port = xml.getAttributeIntValue(null, "port", 0);
                        provider.smtp_starttls = xml.getAttributeBooleanValue(null, "starttls", false);
                    } else
                        throw new IllegalAccessException(name);
                } else if (eventType == XmlPullParser.END_TAG) {
                    if ("provider".equals(xml.getName())) {
                        addSpecials(context, provider);
                        result.add(provider);
                        provider = null;
                    }
                }

                eventType = xml.next();
            }
        } catch (Throwable ex) {
            Log.e(ex);
        }
        final Collator collator = Collator.getInstance(Locale.getDefault());
        collator.setStrength(Collator.SECONDARY); // Case insensitive, process accents etc

        Collections.sort(result, new Comparator<EmailProvider>() {
            @Override
            public int compare(EmailProvider p1, EmailProvider p2) {
                int o = Integer.compare(p1.order, p2.order);
                if (o == 0)
                    return collator.compare(p1.name, p2.name);
                else
                    return o;
            }
        });

        return result;
    }

    static EmailProvider fromDomain(Context context, String domain) throws IOException {
        EmailProvider autoconfig = fromDomainInternal(context, domain);

        List<EmailProvider> providers = loadProfiles(context);
        for (EmailProvider provider : providers)
            if (provider.imap_host.equals(autoconfig.imap_host) ||
                    provider.smtp_host.equals(autoconfig.smtp_host)) {
                Log.i("Replacing autoconfig by profile " + provider.name);
                return provider;
            }

        return autoconfig;
    }

    private static EmailProvider fromDomainInternal(Context context, String domain) throws IOException {
        try {
            Log.i("Provider from DNS domain=" + domain);
            return addSpecials(context, fromDNS(context, domain));
        } catch (Throwable ex) {
            Log.w(ex);
            try {
                Log.i("Provider from ISPDB domain=" + domain);
                return addSpecials(context, fromISPDB(context, domain));
            } catch (Throwable ex1) {
                Log.w(ex1);
                try {
                    Log.i("Provider from template domain=" + domain);
                    return addSpecials(context, fromTemplate(context, domain));
                } catch (Throwable ex2) {
                    Log.w(ex2);
                    throw new UnknownHostException(context.getString(R.string.title_setup_no_settings, domain));
                }
            }
        }
    }

    private static EmailProvider fromISPDB(Context context, String domain) throws IOException, XmlPullParserException {
        EmailProvider provider = new EmailProvider(domain);

        // https://wiki.mozilla.org/Thunderbird:Autoconfiguration:ConfigFileFormat
        URL url = new URL("https://autoconfig.thunderbird.net/v1.1/" + domain);
        Log.i("Fetching " + url);

        HttpURLConnection request = (HttpURLConnection) url.openConnection();
        request.setReadTimeout(ISPDB_TIMEOUT);
        request.setConnectTimeout(ISPDB_TIMEOUT);
        request.setRequestMethod("GET");
        request.setDoInput(true);
        request.connect();

        // https://developer.android.com/reference/org/xmlpull/v1/XmlPullParser
        XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
        XmlPullParser xml = factory.newPullParser();
        xml.setInput(new InputStreamReader(request.getInputStream()));

        boolean imap = false;
        boolean smtp = false;
        String href = null;
        String title = null;
        int eventType = xml.getEventType();
        while (eventType != XmlPullParser.END_DOCUMENT) {
            if (eventType == XmlPullParser.START_TAG) {
                String name = xml.getName();
                if ("displayShortName".equals(name)) {
                    // <displayShortName>GMail</displayShortName>
                    eventType = xml.next();
                    if (eventType == XmlPullParser.TEXT) {
                        String display = xml.getText();
                        Log.i("Name=" + display);
                        provider.name = display;
                    }
                    continue;

                } else if ("incomingServer".equals(name)) {
                    // <incomingServer type="imap">
                    //   <hostname>imap.gmail.com</hostname>
                    //   <port>993</port>
                    //   <socketType>SSL</socketType>
                    //   <username>%EMAILADDRESS%</username>
                    //   <authentication>OAuth2</authentication>
                    //   <authentication>password-cleartext</authentication>
                    // </incomingServer>
                    imap = "imap".equals(xml.getAttributeValue(null, "type"));

                } else if ("outgoingServer".equals(name)) {
                    // <outgoingServer type="smtp">
                    //   <hostname>smtp.gmail.com</hostname>
                    //   <port>465</port>
                    //   <socketType>SSL</socketType>
                    //   <username>%EMAILADDRESS%</username>
                    //   <authentication>OAuth2</authentication>
                    //   <authentication>password-cleartext</authentication>
                    // </outgoingServer>
                    smtp = "smtp".equals(xml.getAttributeValue(null, "type"));

                } else if ("hostname".equals(name)) {
                    eventType = xml.next();
                    if (eventType == XmlPullParser.TEXT) {
                        String host = xml.getText();
                        Log.i("Host=" + host);
                        if (imap)
                            provider.imap_host = host;
                        else if (smtp)
                            provider.smtp_host = host;
                    }
                    continue;

                } else if ("port".equals(name)) {
                    eventType = xml.next();
                    if (eventType == XmlPullParser.TEXT) {
                        String port = xml.getText();
                        Log.i("Port=" + port);
                        if (imap) {
                            provider.imap_port = Integer.parseInt(port);
                            provider.imap_starttls = (provider.imap_port == 143);
                        } else if (smtp) {
                            provider.smtp_port = Integer.parseInt(port);
                            provider.smtp_starttls = (provider.smtp_port == 587);
                        }
                    }
                    continue;

                } else if ("socketType".equals(name)) {
                    eventType = xml.next();
                    if (eventType == XmlPullParser.TEXT) {
                        String socket = xml.getText();
                        Log.i("Socket=" + socket);
                        if ("SSL".equals(socket)) {
                            if (imap)
                                provider.imap_starttls = false;
                            else if (smtp)
                                provider.smtp_starttls = false;
                        } else if ("STARTTLS".equals(socket)) {
                            if (imap)
                                provider.imap_starttls = true;
                            else if (smtp)
                                provider.smtp_starttls = true;
                        } else
                            Log.w("Unknown socket type=" + socket);
                    }
                    continue;

                } else if ("username".equals(name)) {
                    eventType = xml.next();
                    if (eventType == XmlPullParser.TEXT) {
                        String username = xml.getText();
                        Log.i("Username=" + username);
                        if ("%EMAILADDRESS%".equals(username))
                            provider.user = UserType.EMAIL;
                        else if ("%EMAILLOCALPART%".equals(username))
                            provider.user = UserType.LOCAL;
                        else
                            Log.w("Unknown username type=" + username);
                    }
                    continue;

                } else if ("enable".equals(name)) {
                    // <enable visiturl="https://mail.google.com/mail/?ui=2&shva=1#settings/fwdandpop">
                    //   <instruction>You need to enable IMAP access</instruction>
                    // </enable>
                    href = xml.getAttributeValue(null, "visiturl");
                    title = null;

                } else if ("documentation".equals(name)) {
                    // <documentation url="http://mail.google.com/support/bin/answer.py?answer=13273">
                    //   <descr>How to enable IMAP/POP3 in GMail</descr>
                    // </documentation>
                    href = xml.getAttributeValue(null, "url");
                    title = null;

                } else if ("instruction".equals(name) || "descr".equals(name)) {
                    if (href != null) {
                        eventType = xml.next();
                        if (eventType == XmlPullParser.TEXT) {
                            if (title == null)
                                title = "";
                            else
                                title += "<br />";
                            title += xml.getText();
                        }
                        continue;
                    }
                }

            } else if (eventType == XmlPullParser.END_TAG) {
                String name = xml.getName();
                if ("incomingServer".equals(name))
                    imap = false;

                else if ("outgoingServer".equals(name))
                    smtp = false;

                else if ("enable".equals(name) || "documentation".equals(name)) {
                    if (href != null) {
                        if (title == null)
                            title = href;
                        addDocumentation(provider, href, title);
                        href = null;
                        title = null;
                    }
                }
            }

            eventType = xml.next();
        }

        request.disconnect();

        Log.i("imap=" + provider.imap_host + ":" + provider.imap_port + ":" + provider.imap_starttls);
        Log.i("smtp=" + provider.smtp_host + ":" + provider.smtp_port + ":" + provider.smtp_starttls);

        provider.checkValid();

        return provider;
    }

    private static EmailProvider fromDNS(Context context, String domain) throws TextParseException, UnknownHostException {
        // https://tools.ietf.org/html/rfc6186
        SRVRecord imap = lookup(context, "_imaps._tcp." + domain);
        SRVRecord smtp = lookup(context, "_submission._tcp." + domain);

        EmailProvider provider = new EmailProvider(domain);
        provider.imap_host = imap.getTarget().toString(true);
        provider.imap_port = imap.getPort();
        provider.imap_starttls = (provider.imap_port == 143);

        provider.smtp_host = smtp.getTarget().toString(true);
        provider.smtp_port = smtp.getPort();
        provider.smtp_starttls = (provider.smtp_port == 587);

        return provider;
    }

    private static final ExecutorService executor = Executors.newCachedThreadPool();

    private static class Server {
        String host;
        int port;
        Future<Boolean> reachable;

        Server(String domain, String prefix, int port) {
            this.host = (prefix == null ? "" : prefix + ".") + domain;
            this.port = port;

            Log.i("Scanning " + host + ":" + port);
            this.reachable = executor.submit(new Callable<Boolean>() {
                @Override
                public Boolean call() {
                    try (Socket socket = new Socket()) {
                        InetAddress iaddr = InetAddress.getByName(host);
                        InetSocketAddress inetSocketAddress = new InetSocketAddress(iaddr, Server.this.port);
                        socket.connect(inetSocketAddress, DNS_TIMEOUT);
                        Log.i("Reachable " + Server.this);
                        return true;
                    } catch (IOException ex) {
                        Log.i("Unreachable " + Server.this + ": " + Helper.formatThrowable(ex));
                        return false;
                    }
                }
            });
        }

        @Override
        public String toString() {
            return host + ":" + port;
        }
    }

    private static EmailProvider fromTemplate(Context context, String domain)
            throws ExecutionException, InterruptedException, UnknownHostException {
        List<Server> imaps = new ArrayList<>();
        List<Server> smtps = new ArrayList<>();

        // SSL
        imaps.add(new Server(domain, null, 993));
        imaps.add(new Server(domain, "imap", 993));
        imaps.add(new Server(domain, "mail", 993));
        imaps.add(new Server(domain, "mx", 993));
        // STARTTLS
        imaps.add(new Server(domain, null, 143));
        imaps.add(new Server(domain, "imap", 143));
        imaps.add(new Server(domain, "mail", 143));
        imaps.add(new Server(domain, "mx", 143));

        // SSL
        smtps.add(new Server(domain, null, 465));
        smtps.add(new Server(domain, "smtp", 465));
        smtps.add(new Server(domain, "mail", 465));
        smtps.add(new Server(domain, "mx", 465));
        // STARTTLS
        smtps.add(new Server(domain, null, 587));
        smtps.add(new Server(domain, "smtp", 587));
        smtps.add(new Server(domain, "mail", 587));
        smtps.add(new Server(domain, "mx", 587));

        for (Server imap : imaps)
            if (imap.reachable.get())
                for (Server smtp : smtps)
                    if (smtp.reachable.get()) {
                        EmailProvider provider = new EmailProvider();
                        provider.name = domain;

                        provider.imap_host = imap.host;
                        provider.imap_port = imap.port;
                        provider.imap_starttls = (imap.port == 143);

                        provider.smtp_host = smtp.host;
                        provider.smtp_port = smtp.port;
                        provider.smtp_starttls = (smtp.port == 587);

                        return provider;
                    }

        throw new UnknownHostException(domain + " template");
    }

    private static void addDocumentation(EmailProvider provider, String href, String title) {
        if (provider.documentation == null)
            provider.documentation = new StringBuilder();
        else
            provider.documentation.append("<br /><br />");

        provider.documentation.append("<a href=\"").append(href).append("\">").append(title).append("</a>");
    }

    private static EmailProvider addSpecials(Context context, EmailProvider provider) {
        if ("imap.gmail.com".equals(provider.imap_host))
            provider.helpUrl = Helper.FAQ_URI + "#user-content-faq6";

        if (provider.imap_host.endsWith("office365.com") ||
                provider.imap_host.endsWith("live.com"))
            provider.helpUrl = Helper.FAQ_URI + "#user-content-faq14";

        if (provider.imap_host.endsWith("yahoo.com"))
            provider.helpUrl = Helper.FAQ_URI + "#user-content-faq88";

        return provider;
    }

    private static SRVRecord lookup(Context context, String record) throws TextParseException, UnknownHostException {
        Lookup lookup = new Lookup(record, Type.SRV);

        SimpleResolver resolver = new SimpleResolver(ConnectionHelper.getDnsServer(context));
        lookup.setResolver(resolver);
        Log.i("Lookup record=" + record + " @" + resolver.getAddress());
        Record[] records = lookup.run();

        if (lookup.getResult() != Lookup.SUCCESSFUL)
            if (lookup.getResult() == Lookup.HOST_NOT_FOUND ||
                    lookup.getResult() == Lookup.TYPE_NOT_FOUND)
                throw new UnknownHostException(record);
            else
                throw new IllegalArgumentException(lookup.getErrorString());

        SRVRecord result = (records == null || records.length == 0 ? null : (SRVRecord) records[0]);
        Log.i("Found record=" + (records == null ? -1 : records.length) +
                " result=" + (result == null ? "" : result.toString()));
        return result;
    }

    @Override
    public String toString() {
        return name;
    }
}
