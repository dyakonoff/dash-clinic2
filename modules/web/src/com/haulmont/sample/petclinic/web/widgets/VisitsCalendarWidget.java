package com.haulmont.sample.petclinic.web.widgets;

import com.haulmont.addon.dashboard.web.annotation.DashboardWidget;
import com.haulmont.cuba.gui.components.Calendar;
import com.haulmont.cuba.gui.components.HasValue;
import com.haulmont.cuba.gui.components.calendar.SimpleCalendarEvent;
import com.haulmont.cuba.gui.model.CollectionContainer;
import com.haulmont.cuba.gui.model.CollectionLoader;
import com.haulmont.cuba.gui.screen.ScreenFragment;
import com.haulmont.cuba.gui.screen.Subscribe;
import com.haulmont.cuba.gui.screen.UiController;
import com.haulmont.cuba.gui.screen.UiDescriptor;
import com.haulmont.sample.petclinic.entity.visit.Visit;

import javax.inject.Inject;
import java.sql.Date;
import java.util.GregorianCalendar;

import static java.util.Calendar.DAY_OF_MONTH;


@UiController("petclinic_VisitsCalendarWidget")
@UiDescriptor("visits-calendar-widget.xml")
@DashboardWidget(name = "Visits Calendar")
public class VisitsCalendarWidget extends ScreenFragment {

    @Inject
    private CollectionLoader<Visit> visitsDl;
    @Inject
    private CollectionContainer<Visit> visitsDc;
    @Inject
    private Calendar visitsCalendar;

    @Subscribe
    private void onAfterInit(AfterInitEvent event) {
        visitsDl.load();

        for (Visit v : visitsDc.getItems()) {
            SimpleCalendarEvent calendarEvent = new SimpleCalendarEvent();
            calendarEvent.setCaption(v.getPet().getName());
            calendarEvent.setStart(v.getVisitDate());
            calendarEvent.setEnd(v.getVisitDate());
            calendarEvent.setAllDay(true);
            visitsCalendar.getEventProvider().addEvent(calendarEvent);
        }
    }

    @Subscribe("monthPicker")
    private void onMonthPickerValueChange(HasValue.ValueChangeEvent<Date> event) {
        java.util.Calendar c = new GregorianCalendar();
        c.setTime(event.getValue());
        c.add(DAY_OF_MONTH, c.getActualMaximum(DAY_OF_MONTH));
        visitsCalendar.setStartDate((event.getValue()));
        visitsCalendar.setEndDate(c.getTime());
    }
}