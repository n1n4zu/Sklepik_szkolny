package bug.busters.api;

import android.util.Log;

import java.util.List;

import bug.busters.cart.CartItem;
import bug.busters.orders.Order;
import bug.busters.orders.OrderRequest;
import bug.busters.products.Products;
import bug.busters.orders.StatusUpdateRequest;
import bug.busters.users.Users;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Klasa obsługująca komunikację z serwerem przy użyciu interfejsu MyApi
 */
public class Retrofit {

    /**
     * Interfejs opisujący metody komunikacji z serwerem
     */
    public interface ProductCallback {
        void onSuccess(List<Products> products);
        void onFailure(Throwable t);
    }

    /**
     * Interfejs opisujący metody komunikacji z serwerem
     */
    public interface UserCallback {
        void onSuccess(List<Users> users);
        void onFailure(Throwable t);
    }

    /**
     * Interfejs opisujący metody komunikacji z serwerem
     */
    public interface OrderCallback {
        void onSuccess();
        void onFailure(Throwable t);
    }

    /**
     * Interfejs opisujący metody komunikacji z serwerem
     */
    public interface OrdersCallback {
        void onSuccess(List<Order> orders);
        void onFailure(Throwable t);
    }

    /**
     * Interfejs opisujący metody komunikacji z serwerem
     */
    public interface UserOrdersCallback {
        void onSuccess(List<Order> orders);
        void onFailure(Throwable t);
    }

    /**
     * Interfejs opisujący metody komunikacji z serwerem
     */
    public interface StatusUpdateCallback {
        void onSuccess();
        void onFailure(Throwable t);
    }

    /**
     * Metoda pobierająca produkty z serwera
     */
    public void getProducts(ProductCallback callback) {

        retrofit2.Retrofit retrofit = new retrofit2.Retrofit.Builder()
                .baseUrl("http://192.168.0.48:5000/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        MyApi myApi = retrofit.create(MyApi.class);

        Call<List<Products>> call = myApi.getProducts();
        call.enqueue(new Callback<List<Products>>() {
            /**
             * Metoda wywoływana po udanym pobraniu odpowiedzi z serwera
             * @param call zapytanie
             * @param response odpowiedz
             */
            @Override
            public void onResponse(Call<List<Products>> call, Response<List<Products>> response) {
                if (response.isSuccessful()) {
                    List<Products> data = response.body();
                    callback.onSuccess(data);
                } else {
                    callback.onFailure(new Exception("Response not successful"));
                }
            }

            /**
             * Metoda wywoływana w przypadku błędu pobierania odpowiedzi z serwera
             * @param call zapytanie
             * @param t błąd
             */
            @Override
            public void onFailure(Call<List<Products>> call, Throwable t) {
                callback.onFailure(t);
            }
        });
    }


    /**
     * Metoda sprawdziająca poprawność danych logowania
     * @param email E-mail podany przez użytkownika
     * @param password Hasło podane przez użytkownika
     * @param callback Interfejs
     */
    public void getUsers(String email, String password, UserCallback callback) {
        retrofit2.Retrofit retrofit = new retrofit2.Retrofit.Builder()
                .baseUrl("http://192.168.0.48:5000/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        MyApi myApi = retrofit.create(MyApi.class);

        Call<List<Users>> call = myApi.getUsers(email, password);
        call.enqueue(new Callback<List<Users>>() {
            /**
             * Metoda wywoływana po udanym pobraniu odpowiedzi z serwera
             * @param call zapytanie
             * @param response odpowiedź
             */
            @Override
            public void onResponse(Call<List<Users>> call, Response<List<Users>> response) {
                if (response.isSuccessful()) {
                    List<Users> data = response.body();
                    callback.onSuccess(data);
                } else {
                    callback.onFailure(new Exception("Response not successful"));
                }
            }

            /**
             * Metoda wywoływana w przypadku błędu pobierania odpowiedzi z serwera
             * @param call zapytanie
             * @param t błąd
             */
            @Override
            public void onFailure(Call<List<Users>> call, Throwable t) {
                callback.onFailure(t);
            }
        });
    }

    /**
     * Metoda do składania zamówień
     * @param cartItems Lista produktów w koszyku
     * @param userId ID użytkownika
     * @param callback Interfejs
     */
    public void placeOrder(List<CartItem> cartItems, int userId, OrderCallback callback) {
        retrofit2.Retrofit retrofit = new retrofit2.Retrofit.Builder()
                .baseUrl("http://192.168.0.48:5000/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        MyApi myApi = retrofit.create(MyApi.class);

        Call<Void> call = myApi.placeOrder(new OrderRequest(cartItems, userId)); // Przekazanie ID użytkownika
        call.enqueue(new Callback<Void>() {
            /**
             * Metoda wywoływana po udanym pobraniu odpowiedzi z serwera
             * @param call zapytanie
             * @param response odpowiedź
             */
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    callback.onSuccess();
                } else {
                    callback.onFailure(new Exception("Response not successful"));
                }
            }

            /**
             * Metoda wywoływana w przypadku błędu pobierania odpowiedzi z serwera
             * @param call zapytanie
             * @param t błąd
             */
            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                callback.onFailure(t);
            }
        });
    }


    /**
     * Metoda pobierająca zamówienia z serwera
     * @param callback Interfejs
     */
    public void getOrders(OrdersCallback callback) {
        retrofit2.Retrofit retrofit = new retrofit2.Retrofit.Builder()
                .baseUrl("http://192.168.0.48:5000/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        MyApi myApi = retrofit.create(MyApi.class);
        Call<List<Order>> call = myApi.getOrders();

        call.enqueue(new Callback<List<Order>>() {
            /**
             * Metoda wywoływana po udanym pobraniu odpowiedzi z serwera
             * @param call zapytanie
             * @param response odpowiedź
             */
            @Override
            public void onResponse(Call<List<Order>> call, Response<List<Order>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onSuccess(response.body());
                } else {
                    callback.onFailure(new Exception("Response not successful or body is null"));
                }
            }

            /**
             * Metoda wywoływana w przypadku błędu pobierania odpowiedzi z serwera
             * @param call zapytanie
             * @param t błąd
             */
            @Override
            public void onFailure(Call<List<Order>> call, Throwable t) {
                callback.onFailure(t);
                Log.e("Retrofit", "Failed to fetch orders", t);
            }
        });
    }

    /**
     * Metoda pobierająca zamówienia danego użytkownika
     * @param id ID użytkownika
     * @param callback Interfejs
     */
    public void getUserOrders(int id, UserOrdersCallback callback) {
        retrofit2.Retrofit retrofit = new retrofit2.Retrofit.Builder()
                .baseUrl("http://192.168.0.48:5000/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        MyApi myApi = retrofit.create(MyApi.class);
        Call<List<Order>> call = myApi.getUserOrders(id);

        call.enqueue(new Callback<List<Order>>() {
            /**
             * Metoda wywoływana po udanym pobraniu odpowiedzi z serwera
             * @param call zapytanie
             * @param response odpowiedź
             */
            @Override
            public void onResponse(Call<List<Order>> call, Response<List<Order>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onSuccess(response.body());
                } else {
                    callback.onFailure(new Throwable("Nie udało się pobrać zamówień"));
                }
            }

            /**
             * Metoda wywoływana w przypadku błędu pobierania odpowiedzi z serwera
             * @param call zapytanie
             * @param t błąd
             */
            @Override
            public void onFailure(Call<List<Order>> call, Throwable t) {
                callback.onFailure(t);
            }
        });
    }

    /**
     * Metoda zmieniająca status zamówienia
     * @param id_order ID zamówienia
     * @param newStatus Nowy status
     * @param callback Interfejs
     */
    public void changeOrderStatus(int id_order, String newStatus, StatusUpdateCallback callback) {
        retrofit2.Retrofit retrofit = new retrofit2.Retrofit.Builder()
                .baseUrl("http://192.168.0.48:5000/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        MyApi myApi = retrofit.create(MyApi.class);

        StatusUpdateRequest request = new StatusUpdateRequest(id_order, newStatus);

        Call<Void> call = myApi.changeOrderStatus(request);
        call.enqueue(new Callback<Void>() {
            /**
             * Metoda wywoływana po udanym pobraniu odpowiedzi z serwera
             * @param call zapytanie
             * @param response odpowiedź
             */
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    callback.onSuccess();
                } else {
                    callback.onFailure(new Exception("Response not successful"));
                }
            }

            /**
             * Metoda wywoływana w przypadku błędu pobierania odpowiedzi z serwera
             * @param call zapytanie
             * @param t błąd
             */
            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                callback.onFailure(t);
            }
        });
    }
}
