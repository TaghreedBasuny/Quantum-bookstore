
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class QuantumBookstore {

    static class ShippingService {
        static void ship(String address) {
            System.out.println("Quantum book store: Shipping to address: " + address);
        }
    }

    static class MailService {
        static void sendEmail(String email, String message) {
            System.out.println("Quantum book store: Email sent to " + email + " with message: " + message);
        }
    }

    static class PaperBook {
        String ISBN;
        String title;
        int yearPublished;
        double price;
        String author;
        int stock;

        PaperBook(String ISBN, String title, int yearPublished, double price, String author, int stock) {
            this.ISBN = ISBN;
            this.title = title;
            this.yearPublished = yearPublished;
            this.price = price;
            this.author = author;
            this.stock = stock;
        }

        void buy(int quantity, String address) {
            if (stock < quantity) {
                throw new RuntimeException("Quantum book store: Only " + stock + " copies available for " + title);
            }
            stock -= quantity;
            ShippingService.ship(address);
        }
    }

    static class EBook {
        String ISBN;
        String title;
        int yearPublished;
        double price;
        String author;
        String fileType;

        EBook(String ISBN, String title, int yearPublished, double price, String author, String fileType) {
            this.ISBN = ISBN;
            this.title = title;
            this.yearPublished = yearPublished;
            this.price = price;
            this.author = author;
            this.fileType = fileType;
        }

        void buy(String email) {
            MailService.sendEmail(email, "Download your eBook: " + title + "." + fileType);
        }
    }

    static class ShowcaseBook {
        String ISBN;
        String title;
        int yearPublished;
        double price;
        String author;

        ShowcaseBook(String ISBN, String title, int yearPublished, double price, String author) {
            this.ISBN = ISBN;
            this.title = title;
            this.yearPublished = yearPublished;
            this.price = price;
            this.author = author;
        }
    }

    static class Inventory {
        List<PaperBook> paperBooks = new ArrayList<>();
        List<EBook> eBooks = new ArrayList<>();
        List<ShowcaseBook> showcaseBooks = new ArrayList<>();

        void addPaperBook(PaperBook book) {
            paperBooks.add(book);
            System.out.println("Quantum book store: Added paper book - " + book.title);
        }

        void addEBook(EBook book) {
            eBooks.add(book);
            System.out.println("Quantum book store: Added ebook - " + book.title);
        }

        void addShowcaseBook(ShowcaseBook book) {
            showcaseBooks.add(book);
            System.out.println("Quantum book store: Added showcase book - " + book.title);
        }

        double buyBook(String ISBN, int quantity, String email, String address) {
            for (PaperBook book : paperBooks) {
                if (book.ISBN.equals(ISBN)) {
                    book.buy(quantity, address);
                    double total = book.price * quantity;
                    System.out.println("Quantum book store: Purchased " + quantity + " copies of " + book.title);
                    return total;
                }
            }

            for (EBook book : eBooks) {
                if (book.ISBN.equals(ISBN)) {
                    for (int i = 0; i < quantity; i++) {
                        book.buy(email);
                    }
                    double total = book.price * quantity;
                    System.out.println("Quantum book store: Purchased " + quantity + " copies of " + book.title + " (eBook)");
                    return total;
                }
            }

            for (ShowcaseBook book : showcaseBooks) {
                if (book.ISBN.equals(ISBN)) {
                    throw new RuntimeException("Quantum book store: '" + book.title + "' is a showcase book and not for sale.");
                }
            }

            throw new RuntimeException("Quantum book store: Book with ISBN " + ISBN + " not found.");
        }

        void removeOutdatedBooks(int maxAge) {
            int currentYear = Calendar.getInstance().get(Calendar.YEAR);

            List<PaperBook> outdatedPapers = new ArrayList<>();
            for (PaperBook book : paperBooks) {
                if (currentYear - book.yearPublished > maxAge) {
                    outdatedPapers.add(book);
                }
            }
            paperBooks.removeAll(outdatedPapers);
            for (PaperBook b : outdatedPapers) {
                System.out.println("Quantum book store: Removed old paper book - " + b.title);
            }

            List<EBook> outdatedEBooks = new ArrayList<>();
            for (EBook book : eBooks) {
                if (currentYear - book.yearPublished > maxAge) {
                    outdatedEBooks.add(book);
                }
            }
            eBooks.removeAll(outdatedEBooks);
            for (EBook b : outdatedEBooks) {
                System.out.println("Quantum book store: Removed old ebook - " + b.title);
            }

            List<ShowcaseBook> outdatedShowcases = new ArrayList<>();
            for (ShowcaseBook book : showcaseBooks) {
                if (currentYear - book.yearPublished > maxAge) {
                    outdatedShowcases.add(book);
                }
            }
            showcaseBooks.removeAll(outdatedShowcases);
            for (ShowcaseBook b : outdatedShowcases) {
                System.out.println("Quantum book store: Removed old showcase book - " + b.title);
            }
        }
    }

    public static void main(String[] args) {
        Inventory inventory = new Inventory();

        inventory.addPaperBook(new PaperBook("100", "Java Programming", 2020, 150.0, "Ahmed", 3));
        inventory.addEBook(new EBook("200", "Python for Beginners", 2021, 100.0, "Mona", "PDF"));
        inventory.addShowcaseBook(new ShowcaseBook("300", "Sample Book", 2019, 0.0, "Ali"));

        try {
            double total1 = inventory.buyBook("100", 2, "user@example.com", "Nasr City, Cairo");
            System.out.println("Quantum book store: Total paid: " + total1 + " EGP\n");
        } catch (Exception e) {
            System.out.println(e.getMessage() + "\n");
        }

        try {
            double total2 = inventory.buyBook("200", 2, "reader@example.com", "");
            System.out.println("Quantum book store: Total paid: " + total2 + " EGP\n");
        } catch (Exception e) {
            System.out.println(e.getMessage() + "\n");
        }

        try {
            double total3 = inventory.buyBook("300", 1, "demo@example.com", "not needed");
            System.out.println("Quantum book store: Total paid: " + total3 + " EGP\n");
        } catch (Exception e) {
            System.out.println(e.getMessage() + "\n");
        }

        inventory.removeOutdatedBooks(4);
    }
}
