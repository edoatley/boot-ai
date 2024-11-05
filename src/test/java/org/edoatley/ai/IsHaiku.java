package org.edoatley.ai;

import org.assertj.core.util.Strings;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

public class IsHaiku extends TypeSafeMatcher<String> {

    public static final String LINE_BREAK = "\n";

    @Override
    protected boolean matchesSafely(String text) {
        if(Strings.isNullOrEmpty(text) || !text.startsWith("Cat")) {
            return false;
        }

        if (!text.contains(LINE_BREAK)) {
            return false;
        }

        return text.split(LINE_BREAK).length == 3;
    }

    @Override
    public void describeTo(Description description) {
        description.appendText("is a Haiku");
    }

    public static Matcher<String> isHaiku() {
        return new IsHaiku();
    }
}