package com.gon.dashboards.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class EventQueryBuilder implements Serializable {

    private List<Condition> conditions;


    public EventQueryBuilder addCondition(Condition condition) {
        if (conditions == null) {
            conditions = new ArrayList<>();
        }
        this.conditions.add(condition);
        return this;
    }


    public String build() {
        String query = "";

        for (int i = 0; i < conditions.size(); i++) {
            if (i > 0) {
                query += " AND ";
            }
            Condition condition = conditions.get(i);
            query += condition.toString();
        }
        return query;
    }
}
