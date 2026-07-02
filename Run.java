public class Run {
    public static void main(String[] args) {
        // 핵심 부품인 컨트롤러 / 콘솔 화면객체
        LibraryController controller = new LibraryController();
        // 뷰 객체를 생성할 때 컨트롤러 엔진을 인자로 주입 후 넘김
        LibraryConsoleView view = new LibraryConsoleView(controller);

        // 프로그램이 부팅 => 하드디스크 파일 => 과거 데이터 복구
        controller.loadFromFile();

        view.start();
    }
}