package org.remast.baralga.gui.panels.report;

import java.awt.BorderLayout;
import java.awt.Color;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;

import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import org.jdesktop.swingx.JXPanel;
import org.remast.baralga.gui.events.BaralgaEvent;
import org.remast.baralga.gui.model.PresentationModel;
import org.remast.baralga.model.ProjectActivity;
import org.remast.baralga.model.filter.Filter;
import org.remast.swing.util.GuiConstants;

import ca.odell.glazedlists.BasicEventList;
import ca.odell.glazedlists.SortedList;

/**
 * Display and edit the descriptions of all project activities.
 * @author remast
 */
@SuppressWarnings("serial")
public class DescriptionPanel extends JXPanel implements Observer {

    /** The model. */
    private final PresentationModel model;

    /** The list of activities. */
    private final SortedList<ProjectActivity> filteredActivitiesList;

    /** Cache for all entries by activity. */
    private final Map<ProjectActivity, DescriptionPanelEntry> entriesByActivity;

    /** The applied filter. */
    private Filter filter;

    private JPanel container;

    public DescriptionPanel(final PresentationModel model) {
        super();
        this.setLayout(new BorderLayout());
        this.model = model;
        this.filteredActivitiesList = new SortedList<ProjectActivity>(new BasicEventList<ProjectActivity>());
        this.entriesByActivity = new HashMap<ProjectActivity, DescriptionPanelEntry>();
        this.model.addObserver(this);
        this.filter = model.getFilter();

        initialize();
    }

    private void initialize() {
        container = new JPanel();
        container.setLayout(new BoxLayout(container, BoxLayout.Y_AXIS));

        this.add(new JScrollPane(container), BorderLayout.CENTER);

        applyFilter();
    }

    private void applyFilter() {
        // clear filtered activities
        filteredActivitiesList.clear();
        entriesByActivity.clear();

        if (filter != null) {
            final List<ProjectActivity> filteredResult = filter.applyFilters(model.getActivitiesList());
            filteredActivitiesList.addAll(filteredResult);
        } else {
            filteredActivitiesList.addAll(model.getActivitiesList());
        }

        // Remove old description panels.
        container.removeAll();

        for (final ProjectActivity activity : filteredActivitiesList) {
            final DescriptionPanelEntry descriptionPanelEntry = new DescriptionPanelEntry(activity);

            // Alternate background color
            if (filteredActivitiesList.indexOf(activity) % 2 == 0) {
                descriptionPanelEntry.setBackground(Color.WHITE);
            } else {
                descriptionPanelEntry.setBackground(GuiConstants.BEIGE);
            }

            // Save entry
            entriesByActivity.put(activity, descriptionPanelEntry);

            // Display entry
            container.add(descriptionPanelEntry);

        }
    }

    /**
     * Update the panel from observed event.
     */
    public void update(final Observable source, final Object eventObject) {
        if (eventObject == null) {
            return;
        }

        if (!(eventObject instanceof BaralgaEvent)) {
            return;
        }

        final BaralgaEvent event = (BaralgaEvent) eventObject;
        ProjectActivity activity;

        switch (event.getType()) {

            case BaralgaEvent.PROJECT_ACTIVITY_ADDED:
                activity = (ProjectActivity) event.getData();
                DescriptionPanelEntry newEntryPanel = new DescriptionPanelEntry(activity);
                entriesByActivity.put(activity, newEntryPanel);
                this.container.add(newEntryPanel);

                // Set color
                if (entriesByActivity.size() % 2 == 1) {
                    newEntryPanel.setBackground(Color.WHITE);
                } else {
                    newEntryPanel.setBackground(GuiConstants.BEIGE);
                }
                break;

            case BaralgaEvent.PROJECT_ACTIVITY_CHANGED:
                activity = (ProjectActivity) event.getData();
                if (entriesByActivity.containsKey(activity)) {
                    entriesByActivity.get(activity).update();
                }
                break;

            case BaralgaEvent.PROJECT_ACTIVITY_REMOVED:
                activity = (ProjectActivity) event.getData();
                if (entriesByActivity.containsKey(activity)) {
                    DescriptionPanelEntry entryPanel = entriesByActivity.get(activity);
                    this.container.remove(entryPanel);
                }
                break;
        
            case BaralgaEvent.FILTER_CHANGED:
                final Filter newFilter = (Filter) event.getData();
                setFilter(newFilter);
                break;
        }
    }

    private void setFilter(final Filter filter) {
        this.filter = filter;
        applyFilter();
    }
}
