package bug.busters.api;

import java.util.List;

import bug.busters.orders.Order;
import bug.busters.orders.OrderRequest;
import bug.busters.products.Products;
import bug.busters.orders.StatusUpdateRequest;
import bug.busters.users.Users;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
import retrofit2.http.PUT;

/**
 * Interfejs do łączenia się z API na serwerze
 */
public interface MyApi {

    /**
     * Metoda do uzyskania produktów z bazy danych
     * @return Wynik zapytania o pozyskanie produktów
     */
    @GET("getproductsdata")
    Call<List<Products>> getProducts();

    /**
     * Metoda do uzyskania użytkowników z bazy danych
     * @param email E-mail podany przez użytkownika
     * @param password Hasło podane przez użytkownika
     * @return Wynik zapytania o poprawność danych logowania
     */
    @POST("getusersdata")
    Call<List<Users>> getUsers(@Query("email") String email, @Query("password") String password);

    /**
     * Metoda do składania zamówień
     * @param orderRequest dane klasy OrderRequest
     * @return Kod odpowiedzi
     */
    @POST("placeorder")
    Call<Void> placeOrder(@Body OrderRequest orderRequest);

    /**
     * Metoda do uzyskania zamówień
     * @return Wynik zapytania o pozyskanie zamówień
     */
    @GET("getordersdata")
    Call<List<Order>> getOrders();

    /**
     * Metoda do zmiany statusu zamówienia
     * @param statusUpdateRequest dane klasy StatusUpdateRequest
     * @return Kod odpowiedzi
     */
    @PUT("changeorderstatus")
    Call<Void> changeOrderStatus(@Body StatusUpdateRequest statusUpdateRequest);

    /**
     * Metoda do uzyskania zamówień danego użytkownika
     * @param id ID użytkownika
     * @return Wynik zapytania o pozyskanie zamówień użytkownika o podanyn ID
     */
    @GET("getuserorder")
    Call<List<Order>> getUserOrders(@Query("id") int id);
}
