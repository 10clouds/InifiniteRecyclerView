package com.tenclouds.loadingadaptersample.activities;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.v7.widget.RecyclerView;
import android.widget.EditText;

import com.azimolabs.conditionwatcher.ConditionWatcher;
import com.azimolabs.conditionwatcher.Instruction;
import com.tenclouds.infiniterecyclerview.AbstractInfiniteAdapter;
import com.tenclouds.infiniterecyclerview.InfiniteRecyclerView;
import com.tenclouds.loadingadaptersample.R;
import com.tenclouds.loadingadaptersample.utils.ItemsCountInstruction;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.pressImeActionButton;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.contrib.RecyclerViewActions.scrollToPosition;
import static android.support.test.espresso.matcher.ViewMatchers.isAssignableFrom;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static junit.framework.Assert.assertEquals;

@RunWith(AndroidJUnit4.class)
public class MainActivityTest {
    private static final int LOADING_VIEW_ID = -1;
    public static final int EMPTY_VIEW_ID = -2;

    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule = new ActivityTestRule<>(MainActivity.class);

    @Before
    public void setUp(){
        ConditionWatcher.setTimeoutLimit(5000);
    }

    @Test
    public void testLoadingMoreItems() throws Exception {
        InfiniteRecyclerView recyclerView = (InfiniteRecyclerView) mActivityTestRule.getActivity().findViewById(R.id.recycler);
        RecyclerView.Adapter adapter = recyclerView.getAdapter();

        //load first 6 pages of items
        for (int i = 20; i < 120; i += 20) {
            ConditionWatcher.waitForCondition(new ItemsCountInstruction(adapter, i));
            onView(withId(R.id.recycler)).perform(scrollToPosition(i - 1));
        }
    }

    @Test
    public void testShowEmptyMessage() throws Exception {
        InfiniteRecyclerView infiniteRecyclerView = (InfiniteRecyclerView) mActivityTestRule.getActivity().findViewById(R.id.recycler);
        AbstractInfiniteAdapter adapter = (AbstractInfiniteAdapter) infiniteRecyclerView.getAdapter();

        onView(withId(R.id.menu_search)).perform(click());
        onView(isAssignableFrom(EditText.class)).perform(typeText("No such item"), pressImeActionButton());

        assertEquals(1, adapter.getItemCount());
        assertEquals(LOADING_VIEW_ID, adapter.getItemViewType(0));

        ConditionWatcher.waitForCondition(new Instruction() {
            @Override
            public String getDescription() {
                return "Empty view is shown";
            }

            @Override
            public boolean checkCondition() {
                return adapter.getItemViewType(0) == EMPTY_VIEW_ID && adapter.getItemCount() == 1;
            }
        });
    }

}