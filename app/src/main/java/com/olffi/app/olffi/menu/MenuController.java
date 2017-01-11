package com.olffi.app.olffi.menu;

import android.app.Activity;
import android.view.View;

import com.olffi.app.olffi.R;

/**
 * Created by gabrielmorin on 09/01/2017.
 */

public class MenuController {

    private Activity activity;
    private MenuNavigationListener listener;
    private MenuButtonController
            searchMenuButtonController,
            countryRegulationsMenuButtonController,
            coproductionTreatiesMenuButtonController,
            calendarMenuButtonController,
            publicationsMenuButtonController,
            compareMenuButtonController;

    public MenuController(Activity activity) {
        this.activity = activity;
        init();
    }

    private void init() {
        searchMenuButtonController =
                new MenuButtonController(activity, R.id.button_search)
                        .setImageResId(R.drawable.ic_search_white)
                        .setText(activity.getString(R.string.button_search));

        countryRegulationsMenuButtonController =
                new MenuButtonController(activity, R.id.button_country_regulations)
                        .setImageResId(R.drawable.ic_country_regulation_white)
                        .setText(activity.getString(R.string.button_country_regulations));

        coproductionTreatiesMenuButtonController =
                new MenuButtonController(activity, R.id.button_coproduction_treaties)
                        .setImageResId(R.drawable.ic_coproduction_treaties_white)
                        .setText(activity.getString(R.string.button_coproduction_treaties));

        calendarMenuButtonController =
                new MenuButtonController(activity, R.id.button_calendar)
                        .setImageResId(R.drawable.ic_calendar_white)
                        .setText(activity.getString(R.string.button_calendar));

        publicationsMenuButtonController =
                new MenuButtonController(activity, R.id.button_publications)
                        .setImageResId(R.drawable.ic_publications_white)
                        .setText(activity.getString(R.string.button_publications));

        compareMenuButtonController =
                new MenuButtonController(activity, R.id.button_compare)
                        .setImageResId(R.drawable.ic_compare_white)
                        .setText(activity.getString(R.string.button_compare));
    }

    public void setListener(MenuNavigationListener listener) {
        this.listener = listener;
        searchMenuButtonController
                .setOnClickListener(this.listener::onClickSearch);
        countryRegulationsMenuButtonController
                .setOnClickListener(this.listener::onClickCountryRegulations);
        coproductionTreatiesMenuButtonController
                .setOnClickListener(this.listener::onClickCoproductionTreaties);
        calendarMenuButtonController
                .setOnClickListener(this.listener::onClickCalendar);
        publicationsMenuButtonController
                .setOnClickListener(this.listener::onClickPublications);
        compareMenuButtonController
                .setOnClickListener(this.listener::onClickCompare);
    }

    public interface MenuNavigationListener {
        void onClickSearch(View view);
        void onClickCountryRegulations(View view);
        void onClickCoproductionTreaties(View view);
        void onClickCalendar(View view);
        void onClickPublications(View view);
        void onClickCompare(View view);
    }

}
