import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.canvas.parser.PdfTextExtractor;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class BooleanSearchEngine implements SearchEngine {
    private final Map<String, List<PageEntry>> words;
    private static final List<String> stopWords;

    static {
        try {
            stopWords = Files.readAllLines(Paths.get("stop-ru.txt"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public BooleanSearchEngine(File pdfsDir) throws IOException {
        List<File> PDFList = List.of(Objects.requireNonNull(pdfsDir.listFiles()));
        words = new HashMap<>();
        for (File pdf : PDFList) {
            var doc = new PdfDocument(new PdfReader(pdf));
            for (int i = 0; i < doc.getNumberOfPages(); i++) {
                var text = PdfTextExtractor.getTextFromPage(doc.getPage(i + 1));
                var words = Stream.of(text.toLowerCase().split("\\P{IsAlphabetic}+"))
                        .collect(Collectors.toCollection(ArrayList<String>::new));
                words.removeAll(stopWords);

                Map<String, Integer> freqs = new HashMap<>();
                for (var word : words) {
                    if (word.isEmpty()) {
                        continue;
                    }
                    freqs.put(word.toLowerCase(), freqs.getOrDefault(word.toLowerCase(), 0) + 1);
                }
                int count;
                for (var word : freqs.keySet()) {
                    if (freqs.get(word.toLowerCase()) != null) {
                        count = freqs.get(word.toLowerCase());
                        this.words.computeIfAbsent(word.toLowerCase(), w -> new ArrayList<>()).add(new PageEntry(pdf.getName(), i + 1, count));
                    }
                }
                freqs.clear();
            }
        }

    }

    @Override
    public List<PageEntry> search(String word) {
        List<PageEntry> tempList = new ArrayList<>();
        List<PageEntry> result = new ArrayList<>();
        String[] test = word.split("\\P{IsAlphabetic}+");
        for (String tests : test) {
            if (words.get(tests.toLowerCase()) != null) {
                tempList.addAll(words.get(tests.toLowerCase()));
            }
        }
        Map<String, Map<Integer, Integer>> map = new HashMap<>();
        for (PageEntry pageEntry : tempList) {
            map.computeIfAbsent(pageEntry.getPdfName(), key -> new HashMap<>()).merge(pageEntry.getPage(), pageEntry.getCount(), Integer::sum);
        }

        map.forEach((key, value) -> {
            for (var tempPage : value.entrySet()) {
                result.add(new PageEntry(key, tempPage.getKey(), tempPage.getValue()));
            }
        });
        Collections.sort(result);
        return result;

    }
}
