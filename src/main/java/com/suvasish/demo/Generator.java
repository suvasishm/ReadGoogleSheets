package com.suvasish.demo;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.model.ValueRange;
import com.suvasish.demo.model.DemoModel;

import java.io.File;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class Generator {
    private static ResourceBundle RESOURCE = ResourceBundle.getBundle("input");
    private static ObjectMapper mapper = new ObjectMapper();

    public static void main(String[] args) throws IOException {
        // authorize
        Sheets service = SheetsUtil.getSheetsService();

        // read from the sheet
        ValueRange response = service.spreadsheets().values()
                .get(RESOURCE.getString("sheet.id"), RESOURCE.getString("sheet.range"))
                .execute();
        List<List<Object>> values = response.getValues();

        List<DemoModel> routeOfAdminLkps = new ArrayList<>();
        for (List row : values) {
            String formatted = MessageFormat.format(
                    // Todo - update this as per json.template
                RESOURCE.getString("json.template"), row.get(0), row.get(1));

            // objectify
            // Todo - Create and DemoModel.java as per requirement
            DemoModel routeOfAdmin = mapper.readValue(formatted, DemoModel.class);
            routeOfAdminLkps.add(routeOfAdmin);
        }

        mapper.writeValue(new File(RESOURCE.getString("target.path")), routeOfAdminLkps);
    }
}
