import java.io.*;
import java.util.*;

public class LibraryController {
    
    private List<Book> bookList = new ArrayList<>();
    private Map<String, Member> memberMap = new HashMap<>(); 
    
    // 파일 이름 설정
    private final String FILE_NAME = "library_data.dat";

    // [도서 등록 기능] View가 전달 -> 객체를 List 맨 뒤
    public void registerBook(Book book) {
        bookList.add(book);
    }

    // [회원 등록 기능]
    public boolean registerMember(Member member) {
        if (memberMap.containsKey(member.getMemberId())) {
            return false; 
        }
        memberMap.put(member.getMemberId(), member);
        return true; 
    }

    // [회원 삭제]
    public boolean deleteMember(String memberId) {
        Member member = memberMap.get(memberId);
        
        if (member == null) return false;
        if (member.getBorrowedCount() > 0) return false; 
        
        memberMap.remove(memberId); 
        return true; 
    }

    // View가 List에 전달
    public List<Book> getBookList() {
        return bookList;
    }

    // [도서 대출 처리]
    public boolean processBorrow(String memberId, String bookId) {
        // ID입력 -> Map에서 회원 정보를 꺼냄
        Member member = memberMap.get(memberId);
        
        // 등록되지 않은 ID, 책을 3권 이상 빌리면 대출 거부
        if (member == null || member.getBorrowedCount() >= 3) {
            return false;
        }

        for (Book book : bookList) {
            if (book.getBookId().equals(bookId) && !book.isBorrowed()) {
                book.borrowBook(memberId, null); // 1. 해당 책의 상태를 대출중으로 바꾸고 대출자 ID
                member.increaseBorrowedCount();  // 2. 해당 회원의 대출 장부 권수를 1권 증가
                return true; 
            }
        }
        return false; 
    }

    // [도서 반납 처리 로직]
    public boolean processReturn(String bookId) {
        //List에서 책 찾음
        for (Book book : bookList) {
            // 사용자가 입력한 책 번호가 맞고 && 그 책이 현재 대출 중(isBorrowed)인 책이 맞다면 반납 시작!
            if (book.getBookId().equals(bookId) && book.isBorrowed()) {
                // 이 책을 빌려갔던 대출자 ID알아냄 -> Map에서 그 회원ID 꺼내옴
                Member member = memberMap.get(book.getBorrowerId());
                
                book.returnBook(); // 책을 다시 대출 가능한 상태로 리셋
                
                if (member != null) {
                    member.decreaseBorrowedCount(); // 그 회원의 대출 장부 권수를 1권 차감
                }
                return true; 
            }
        }
        return false; 
    }

    // [도서 삭제 기능]
    public boolean deleteBook(String bookId) {
        for (int i = 0; i < bookList.size(); i++) {
           
            if (bookList.get(i).getBookId().equals(bookId)) {
               
                if (bookList.get(i).isBorrowed()) return false; 
                
                bookList.remove(i); // 대출 없음 -> 지움(.remove)
                return true;
            }
        }
        return false; 
    }

    // [파일 영구 저장]
    public void saveToFile() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_NAME))) {
            oos.writeObject(bookList);  // 도서 저장
            oos.writeObject(memberMap); // 회원 저장
        } catch (IOException e) {
            System.out.println("파일 저장 실패: " + e.getMessage());
        }
    }

    // [파일 데이터 로드] 
    @SuppressWarnings("unchecked")
    public void loadFromFile() {
        File file = new File(FILE_NAME);
        if (!file.exists()) return; 

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(FILE_NAME))) {
            bookList = (List<Book>) ois.readObject(); 
            memberMap = (Map<String, Member>) ois.readObject(); 
        } catch (Exception e) {
            System.out.println("데이터 로드 실패: " + e.getMessage());
        }
    }
}