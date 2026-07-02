
import java.io.Serializable;
import java.time.LocalDate;

// Serializable 인터페이스를 구현해야 이 객체(책 정보)를 파일에 통째로 저장할 수 있음
public class Book implements Serializable {
    
    // 책 한 권이 가져야 할 필수 정보
    private String bookId;       // 도서 고유 번호
    private String title;        // 도서 제목
    private boolean isBorrowed;  // 대출 여부 상태
    private String borrowerId;   // 대풀한 회원의 고유 ID (아무도 안 빌렸으면 null)
    private LocalDate dueDate;   // 반납 예정일 (이번 미니 프로젝트 기능 축소로 인해 항상 null 유지)

    
    public Book(String bookId, String title) {
        this.bookId = bookId;
        this.title = title;
        this.isBorrowed = false; 
        this.borrowerId = null; 
        this.dueDate = null;     
    }

    // [대출 실행 메서드] 3번 메뉴에서 대출 성공 시
    public void borrowBook(String memberId, LocalDate date) {
        this.isBorrowed = true;      // 대출 상태 -> 대출중
        this.borrowerId = memberId;  // 빌려간 회원의 ID를 기록합니다.
        this.dueDate = date;         // (null을 보내줄 예정)
    }

    // [반납 실행 메서드] 4번 메뉴에서 반납 성공 시 이 메서드를 실행해 책 정보를 리셋합니다.
    public void returnBook() {
        this.isBorrowed = false;     // 대출 상태 -> 대출가능
        this.borrowerId = null;      // 빌려간 사람의 정보 삭제
        this.dueDate = null;         // 반납 예정일 삭제
    }

    // [Getter 메서드]
    public String getBookId() { return bookId; }
    public String getTitle() { return title; }
    public boolean isBorrowed() { return isBorrowed; }
    public String getBorrowerId() { return borrowerId; }
    public LocalDate getDueDate() { return dueDate; }
}