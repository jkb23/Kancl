package online.kancl.util;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class HashUtilsTest {

    @Test
    void sha256HashTest() {
        assertThat(HashUtils.sha256Hash("qwerty"))
                .isEqualTo("65e84be33532fb784c48129675f9eff3a682b27168c0ea744b2cf58ee02337c5");

        assertThat(HashUtils.sha256Hash("pomnu"))
                .isEqualTo("a13a2db87affb4cf610b54cecfdcedbb3e950ff3a8aa34e6bcb2bbf41855ebbc");

        assertThat(HashUtils.sha256Hash("zxcv852"))
                .isEqualTo("18c24f1f1016f348a38d16a00ec394b49ccc7bd86d7d6a44a2baf295a48231a1");

        assertThat(HashUtils.sha256Hash(""))
                .isEqualTo("e3b0c44298fc1c149afbf4c8996fb92427ae41e4649b934ca495991b7852b855");

        assertThat(HashUtils.sha256Hash("QaZ741"))
                .isEqualTo("dfc542dc58aade2f2792c5ab9f2eecc678ec01ef1b93bb0990c32d714a67ca22");
    }
}