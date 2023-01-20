package api;

import org.json.JSONObject;

import java.util.List;

public class ApiHelpers {

    public static String getFormattedResult(List<String> list) {
        return list
                .toString()
                .replaceAll(".$", "")
                .replaceAll("^.", "");
    }

    public static String getEightDaysForecastCalendar(JSONObject obj) {
        StringBuilder sb = new StringBuilder();
        for (int j = 0; j < 3; j++) {
            sb
                    .append(new java.util.Date((
                                    (obj
                                            .getJSONArray("daily")
                                            .getJSONObject(0)
                                            .getLong("dt")) * 1000)
                            )
                                    .toString()
                                    .split(" ")[j]
                    )
                    .append(" ");
        }

        return sb.toString();
    }

    public static String getCurrentTemp(JSONObject obj) {
        StringBuilder sb = new StringBuilder();

        sb.append(Math.round(obj.getJSONObject("current")
                .getDouble("temp")));

        return sb.toString().concat("°C");
    }
}
