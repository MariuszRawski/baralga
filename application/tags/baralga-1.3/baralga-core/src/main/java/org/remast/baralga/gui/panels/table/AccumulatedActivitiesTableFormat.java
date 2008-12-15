package org.remast.baralga.gui.panels.table;

import org.remast.baralga.Messages;
import org.remast.baralga.model.report.AccumulatedProjectActivity;

import ca.odell.glazedlists.gui.TableFormat;

/**
 * Table format for accumulated activities.
 * 
 * @author remast
 */
public class AccumulatedActivitiesTableFormat implements TableFormat<AccumulatedProjectActivity> {

    /**
     * Gets the number of columns for the table.
     */
    public int getColumnCount() {
        return 3;
    }

    /**
     * Gets the name of the given column.
     * @param column the number of the column
     */
    public String getColumnName(int col) {
        switch (col) {
            case 0:
                return Messages.getString("AccumulatedActivitiesTableFormat.DayHeading"); //$NON-NLS-1$
            case 1:
                return Messages.getString("AccumulatedActivitiesTableFormat.ProjectHeading"); //$NON-NLS-1$
            case 2:
                return Messages.getString("AccumulatedActivitiesTableFormat.DurationHeading"); //$NON-NLS-1$
            default:
                return ""; //$NON-NLS-1$
        }
    }

    public Object getColumnValue(AccumulatedProjectActivity accActivity, int col) {
        switch (col) {
            case 0:
                return accActivity.getDay();
            case 1:
                return accActivity.getProject();
            case 2:
                return accActivity.getTime();
            default:
                return ""; //$NON-NLS-1$
        }
    }

}