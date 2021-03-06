/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package tech.tablesaw.plotting.fx;

import javafx.collections.FXCollections;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.layout.Background;
import tech.tablesaw.api.NumberColumn;
import tech.tablesaw.api.StringColumn;
import tech.tablesaw.api.Table;
import tech.tablesaw.columns.Column;

import java.util.ArrayList;
import java.util.List;

/**
 * A JavaFx-based Horizontal bar chart
 */
public class FxHorizontalBar extends FxBuilder {

    public static BarChart<Number, String> chart(
            String title,
            Table table,
            String categoryColumnName,
            String numberColumnName) {

        StringColumn stringColumn = table.stringColumn(categoryColumnName);
        NumberColumn numberColumn = table.nCol(numberColumnName);

        return chart(title, stringColumn, numberColumn);
    }

    public static BarChart<Number, String> chart(String title, StringColumn stringColumn, NumberColumn
            numberColumn) {

        final CategoryAxis categoryAxis = getCategoryAxis(stringColumn);
        final NumberAxis numberAxis = getNumberAxis(numberColumn);

        final BarChart<Number, String> bar = getNumberStringBarChart(title, numberAxis, categoryAxis);
        List<XYChart.Data<Number, String>> d2 = new ArrayList<>(numberColumn.size());

        for (int i = 0; i < numberColumn.size(); i++) {
            d2.add(new XYChart.Data<>(numberColumn.get(i), stringColumn.get(i)));
        }

        XYChart.Series<Number, String> series1 = getNumberStringSeries(stringColumn, d2);

        bar.getData().add(series1);
        return bar;
    }

    public static BarChart<Number, String> chart(String title, NumberColumn categoryColumn, NumberColumn numberColumn) {

        final CategoryAxis categoryAxis = getCategoryAxis(categoryColumn);
        final NumberAxis numberAxis = getNumberAxis(numberColumn);

        final BarChart<Number, String> bar = getNumberStringBarChart(title, numberAxis, categoryAxis);
        List<XYChart.Data<Number, String>> d2 = new ArrayList<>(numberColumn.size());

        for (int i = 0; i < numberColumn.size(); i++) {
            d2.add(new XYChart.Data<>(numberColumn.get(i), categoryColumn.getString(i)));
        }

        XYChart.Series<Number, String> series1 = getNumberStringSeries(categoryColumn, d2);

        bar.getData().add(series1);
        return bar;
    }

    private static XYChart.Series<Number, String> getNumberStringSeries(Column categoryColumn, List<XYChart
            .Data<Number, String>> d2) {
        XYChart.Series<Number, String> series1 = new XYChart.Series<>(FXCollections.observableList(d2));
        series1.setName(categoryColumn.name());
        return series1;
    }

    private static BarChart<Number, String> getNumberStringBarChart(String title, NumberAxis xAxis, CategoryAxis
            yAxis) {
        final BarChart<Number, String> bar = new BarChart<>(xAxis, yAxis);
        bar.setTitle(title);
        bar.setLegendVisible(false);
        bar.setCategoryGap(0.0);
        bar.setBarGap(0.25);
        bar.setBackground(Background.EMPTY);
        bar.setHorizontalGridLinesVisible(false);
        return bar;
    }
}

