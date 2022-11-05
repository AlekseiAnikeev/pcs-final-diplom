public class Main {
    public static final int PORT = 8989;
    private static final String PATH_PDF_FILE = "pdfs";
    public static String STOP_WORDS_FILE_PATH = "stop-ru.txt";

    public static void main(String[] args) throws Exception {
//        BooleanSearchEngine engine = new BooleanSearchEngine(new File("pdfs"));
//        System.out.println(new Gson().toJson(engine.search("DevOps контроля")));
        SearchServer server = new SearchServer(PORT, PATH_PDF_FILE);
        server.start();
    }
}