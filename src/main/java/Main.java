public class Main {
    public static final int PORT = 8989;
    public static void main(String[] args) throws Exception {
//        BooleanSearchEngine engine = new BooleanSearchEngine(new File("pdfs"));
//        System.out.println(new Gson().toJson(engine.search("бизнес")));
        SearchServer server = new SearchServer(PORT);
        server.start();
    }
}