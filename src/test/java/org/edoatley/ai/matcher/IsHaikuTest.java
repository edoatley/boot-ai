package org.edoatley.ai.matcher;

import org.junit.jupiter.api.Test;

import static org.edoatley.ai.matcher.IsHaiku.isHaiku;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.not;

class IsHaikuTest {

    @Test
    void shouldMatchValidHaiku() {
        String haiku = """
                Cat on windowsill
                Watching birds fly past slowly
                Dreams of catching them""";
        
        assertThat(haiku, isHaiku());
    }

    @Test
    void shouldNotMatchWhenDoesNotStartWithCat() {
        String notHaiku = """
                Dog on windowsill
                Watching birds fly past slowly
                Dreams of catching them""";
        
        assertThat(notHaiku, not(isHaiku()));
    }

    @Test
    void shouldNotMatchWhenNotThreeLines() {
        String twoLines = """
                Cat on windowsill
                Watching birds fly past slowly""";
        
        assertThat(twoLines, not(isHaiku()));
    }

    @Test
    void shouldNotMatchEmptyString() {
        assertThat("", not(isHaiku()));
    }
}