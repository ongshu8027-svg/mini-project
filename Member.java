
import java.io.Serializable;
import java.time.LocalDate;

// 회원 정보 역시 파일에 통째로 저장하고 불러오는 곳
public class Member implements Serializable {
    
    // [속성/데이터] 회원 한 명의 정보
    private String memberId;           // 회원 고유 번호 ID
    private String name;               // 회원 이름
    private int borrowedCount;         // 책 대출권수 (최대 3권 한도 체크용)
    private LocalDate penaltyEndDate;  // 연체 패널티 종료일 (기능 제외 null 유지)

  
    public Member(String memberId, String name) {
        this.memberId = memberId;
        this.name = name;
        this.borrowedCount = 0;       
        this.penaltyEndDate = null;   
    }

    // [대출 자격 검증] 회원이 현재 대출 가능한 상태인지 검사
    public boolean isEligibleToBorrow() {
        if (borrowedCount >= 3) return false; // 3권을 빌린 상태 -> 대출 불가 처리
        return true; // 3권 미만 -> 대출 가능 처리
    }

    // [대출 권수 증가] 대출이 최종 성공 -> 빌린 책 개수를 1개 늘림
    public void increaseBorrowedCount() { 
        this.borrowedCount++; 
    }
    
    // [대출 권수 감소] 반납이 최종 성공 -> 빌린 책 개수를 1개 줄임
    public void decreaseBorrowedCount() { 
        this.borrowedCount--; 
    }

    // [Getter 메서드들] Controller or View가 데이터를 읽어갈 수 있도록 제공
    public String getMemberId() { return memberId; }
    public String getName() { return name; }
    public int getBorrowedCount() { return borrowedCount; }
    public LocalDate getPenaltyEndDate() { return penaltyEndDate; }
}