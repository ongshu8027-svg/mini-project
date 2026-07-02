import java.util.List;
import java.util.Scanner;

public class LibraryConsoleView {
    // LibraryController를 필드로 가지고 있어야 기능을 호출할 때 사용가능
    private LibraryController controller;
    private Scanner scanner = new Scanner(System.in);

    public LibraryConsoleView(LibraryController controller) {
        this.controller = controller;
    }

    
    public void start() {
      
        printMessage("도서관 시스템 가동 (사용 가능 ID: M001, M002 등 자유롭게 등록하세요!)");


        while (true) {
            int menu = showMainMenu(); 
            if (menu == 1) { // [1번 선택]: 도서 등록
                Book book = inputBookInfo();
                controller.registerBook(book);   
                printMessage("도서가 등록되었습니다.");
                
            } else if (menu == 2) { // [2번 선택]: 도서 현황 전체 조회
                printBookList(controller.getBookList());
                
            } else if (menu == 3) { // [3번 선택]: 도서 대출
                String mId = inputFieldInfo("대출할 회원 ID");
                String bId = inputFieldInfo("대출할 도서 번호");
                
                if (controller.processBorrow(mId, bId)) {
                    printMessage("대출 성공!");
                } else {
                    printMessage("대출 실패! (ID 오류, 존재하지 않는 도서, 혹은 3권 초과 대출 상태 확인)");
                }
                
            } else if (menu == 4) { // [4번 선택]: 도서 반납 
                String bId = inputFieldInfo("반납할 도서 번호");
                
                if (controller.processReturn(bId)) {
                    printMessage("반납 완료되었습니다.");
                } else {
                    printMessage("반납 실패! (대출 중인 올바른 도서 번호가 아닙니다.)");
                }
                
            } else if (menu == 5) { // [5번 선택]: 도서 삭제
                String bId = inputFieldInfo("삭제할 도서 번호");
                
                if (controller.deleteBook(bId)) {
                    printMessage("도서가 정상적으로 삭제되었습니다.");
                } else {
                    printMessage("삭제 실패! (대출 중인 도서는 반납 전까지 지울 수 없습니다.)");
                }
                
            } else if (menu == 6) { // [6번 선택]: 회원 등록
                Member member = inputMemberInfo();
                if (controller.registerMember(member)) {
                    printMessage("회원 등록이 완료되었습니다.");
                } else {
                    printMessage("등록 실패! (이미 존재하는 회원 ID입니다.)");
                }

            } else if (menu == 7) { // [7번 선택]: 회원 탈퇴
                String mId = inputFieldInfo("삭제할 회원 ID");
                if (controller.deleteMember(mId)) {
                    printMessage("회원 정보가 성공적으로 삭제되었습니다.");
                } else {
                    printMessage("삭제 실패! (존재하지 않는 ID이거나, 아직 대출 중인 도서가 있는 회원은 삭제할 수 없습니다.)");
                }

            } else if (menu == 0) { // [0번 선택]: 종료 및 데이터백업
                controller.saveToFile(); // 백업 후 저장
                printMessage("데이터를 안전하게 저장하고 프로그램을 완전히 종료합니다.");
                break;
                
            } else { 
                printMessage("잘못된 메뉴 번호입니다. 0번에서 7번 사이를 입력하세요.");
            }
        }
    }

    // [메인 메뉴 화면 출력]
    public int showMainMenu() {
        System.out.println("\n===== 도서관 관리 시스템 =====");
        System.out.println("1. 도서 등록 ");
        System.out.println("2. 도서 현황 조회 ");
        System.out.println("3. 도서 대출 ");
        System.out.println("4. 도서 반납 ");
        System.out.println("5. 도서 삭제 ");
        System.out.println("6. 신규 회원 등록");
        System.out.println("7. 기존 회원 삭제");
        System.out.println("0. 프로그램 종료 및 저장");
        System.out.print("메뉴 선택: ");
        int selected = scanner.nextInt();
        scanner.nextLine();
        return selected;
    }

    // [도서 등록 입력 화면] 1번을 눌렀을 때 작동
    public Book inputBookInfo() {
        System.out.print("등록할 도서 번호: ");
        String id = scanner.nextLine();
        System.out.print("등록할 도서 제목: ");
        String title = scanner.nextLine();
        return new Book(id, title); 
    }

    // [회원 등록 입력 화면] 6번을 눌렀을 때 작동
    public Member inputMemberInfo() {
        System.out.print("등록할 회원 ID: ");
        String id = scanner.nextLine();
        System.out.print("등록할 회원 이름: ");
        String name = scanner.nextLine();
        return new Member(id, name);
    }

    // [공통 문자열 입력 화면] 기존 inputDeleteInfo에서 범용적인 명칭으로 변경
    public String inputFieldInfo(String msg) {
        System.out.print(msg + " 입력: ");
        return scanner.nextLine();
    }

    // [도서 현황 출력 화면] 2번을 눌렀을 때 작동
    public void printBookList(List<Book> bookList) {
        System.out.println("\n[도서 현황 목록]");
        System.out.println("-------------------------------------------------------");
        System.out.println("도서번호\t| 제목\t\t| 대출여부\t| 대출자ID\t");
        System.out.println("-------------------------------------------------------");
        
        if (bookList.isEmpty()) {
            System.out.println("등록된 도서가 존재하지 않습니다.");
        } else {
            for (Book b : bookList) {
                String status = b.isBorrowed() ? "대출중" : "대출가능";
                String borrower = b.getBorrowerId() != null ? b.getBorrowerId() : "-";
                String date = b.getDueDate() != null ? b.getDueDate().toString() : "-";
                
                System.out.println(b.getBookId() + "\t| " + b.getTitle() + "\t| " + status + "\t| " + borrower + "\t| " + date);
            }
        }
        System.out.println("-------------------------------------------------------");
    }

    // 알림 메시지
    public void printMessage(String msg) {
        System.out.println(msg);
    }
}