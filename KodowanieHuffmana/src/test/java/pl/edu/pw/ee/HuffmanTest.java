package pl.edu.pw.ee;

import java.io.FileNotFoundException;
import java.io.IOException;
import org.junit.Test;

public class HuffmanTest {

    @Test(expected = IllegalArgumentException.class)
    public void polishCharTest() throws IOException {
        Huffman tst = new Huffman();
        tst.huffman("C:\\Users\\Filip\\Desktop\\Repo\\2022Z_AISD_git_repo_GR1_gr18\\aisd_lab_7_huffman\\src\\test\\java\\pl\\edu\\pw\\ee\\Polskie_znaki", true);

        assert false;
    }

    @Test
    public void niemanieTest() throws IOException {
        Huffman tst = new Huffman();
        tst.huffman("C:\\Users\\Filip\\Desktop\\Repo\\2022Z_AISD_git_repo_GR1_gr18\\aisd_lab_7_huffman\\src\\test\\java\\pl\\edu\\pw\\ee\\Niemanie_bez_polskich", true);
        tst.huffman("C:\\Users\\Filip\\Desktop\\Repo\\2022Z_AISD_git_repo_GR1_gr18\\aisd_lab_7_huffman\\src\\test\\java\\pl\\edu\\pw\\ee\\Niemanie_bez_polskich", false);
    }

//    @Test
//    public void panTadeuszTest() throws IOException{
//        Huffman tst = new Huffman();
//        tst.huffman("C:\\Users\\Filip\\Desktop\\Repo\\2022Z_AISD_git_repo_GR1_gr18\\aisd_lab_7_huffman\\src\\test\\java\\pl\\edu\\pw\\ee\\Pan_Tadeusz", true);
//        tst.huffman("C:\\Users\\Filip\\Desktop\\Repo\\2022Z_AISD_git_repo_GR1_gr18\\aisd_lab_7_huffman\\src\\test\\java\\pl\\edu\\pw\\ee\\Pan_Tadeusz", false);
//    }
    @Test
    public void oneCharTest() throws IOException {
        Huffman tst = new Huffman();
        tst.huffman("C:\\Users\\Filip\\Desktop\\Repo\\2022Z_AISD_git_repo_GR1_gr18\\aisd_lab_7_huffman\\src\\test\\java\\pl\\edu\\pw\\ee\\Jedna_litera", true);
        tst.huffman("C:\\Users\\Filip\\Desktop\\Repo\\2022Z_AISD_git_repo_GR1_gr18\\aisd_lab_7_huffman\\src\\test\\java\\pl\\edu\\pw\\ee\\Jedna_litera", false);
    }

    @Test(expected = IllegalArgumentException.class)
    public void emptyFileTest() throws IOException {
        Huffman tst = new Huffman();
        tst.huffman("C:\\Users\\Filip\\Desktop\\Repo\\2022Z_AISD_git_repo_GR1_gr18\\aisd_lab_7_huffman\\src\\test\\java\\pl\\edu\\pw\\ee\\Pusty_plik", true);
        tst.huffman("C:\\Users\\Filip\\Desktop\\Repo\\2022Z_AISD_git_repo_GR1_gr18\\aisd_lab_7_huffman\\src\\test\\java\\pl\\edu\\pw\\ee\\Pusty_plik", false);

        assert false;
    }

    @Test(expected = IllegalArgumentException.class)
    public void nullTest() throws IOException {
        Huffman tst = new Huffman();
        tst.huffman(null, true);

        assert false;
    }

    @Test(expected = FileNotFoundException.class)
    public void wrongPathTest() throws IOException {
        Huffman tst = new Huffman();
        tst.huffman("piesek", true);

        assert false;
    }

}
