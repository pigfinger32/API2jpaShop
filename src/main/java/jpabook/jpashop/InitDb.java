package jpabook.jpashop;

import jpabook.jpashop.domain.*;
import jpabook.jpashop.domain.item.Book;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.persistence.Entity;
import javax.persistence.EntityManager;

/**
 * 종 주문 2개
 * * userA
 * 	 * JPA1 BOOK
 * 	 * JPA2 BOOK
 * * userB
 * 	 * SPRING1 BOOK
 * 	 * SPRING2 BOOK
 */
@Component
@Transactional
@RequiredArgsConstructor
public class InitDb {
    private final InitService initService;
    @PostConstruct
    public void Init() {

        initService.dbInit1();
        initService.dbInit2();
    }
    @Component
    @Transactional
    @RequiredArgsConstructor
    static class InitService {
        private final EntityManager em;
        public void dbInit1() {
            Member member = new Member();
            member.setName("userA");
            member.setAddress(new Address("서울", "1", "111"));
            em.persist(member);

            Book book1 = new Book();
            book1.setName("JPA1 BOOK");
            book1.setPrice(10000);
            book1.setStockQuantity(100);
            em.persist(book1);

            Book book2 = new Book();
            book2.setName("JPA2 BOOK");
            book2.setPrice(20000);
            book2.setStockQuantity(100);
            em.persist(book2);

            Delivery delivery = createDelivery(member);


            OrderItem orderItem1 = OrderItem.createOrderItem(book1, 10000, 1);
            OrderItem orderItem2 =OrderItem.createOrderItem(book2, 20000, 2);

            Order order = Order.createOrder(member, delivery, orderItem1, orderItem2);
            em.persist(order);


        }
        public void dbInit2() {
            Member member = createMember("userA", "진주", "2", "222");
            em.persist(member);

            Book book1 = createBook("SPRING1 BOOK", 20000, 200);
            em.persist(book1);
            
            Book book2 = createBook("SPRING2 BOOK", 40000, 300);
            em.persist(book2);

            Delivery delivery = createDelivery(member);

            OrderItem orderItem1 = OrderItem.createOrderItem(book1, 10000, 1);
            OrderItem orderItem2 =OrderItem.createOrderItem(book2, 20000, 2);

            delivery.setAddress(member.getAddress());
            Order order = Order.createOrder(member, delivery, orderItem1, orderItem2);
            em.persist(order);
        }

    }

    private static Delivery createDelivery(Member member) {
        Delivery delivery = new Delivery();
        delivery.setAddress(member.getAddress());
        return delivery;
    }

    private static Member createMember(String name, String city, String street, String zipcode) {
        Member member = new Member();
        member.setName(name);
        member.setAddress(new Address(city, street, zipcode));
        return member;
    }

    private static Book createBook(String name, int price, int stockQuantity) {
        Book book = new Book();
        book.setName(name);
        book.setPrice(price);
        book.setStockQuantity(stockQuantity);
        return book;
    }
    
    
}

