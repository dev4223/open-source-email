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

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

@Dao
public interface DaoRule {
    @Query("SELECT * FROM rule")
    List<EntityRule> getRules();

    @Query("SELECT COUNT(*) FROM rule WHERE folder = :folder")
    int countRules(long folder);

    @Query("SELECT * FROM rule WHERE id = :id")
    EntityRule getRule(long id);

    @Query("SELECT * FROM rule")
    LiveData<List<EntityRule>> liveRules();

    @Insert
    long insertRule(EntityRule rule);

    @Update
    int updateRule(EntityRule rule);

    @Query("DELETE FROM rule WHERE id = :id")
    void deleteRule(long id);
}
