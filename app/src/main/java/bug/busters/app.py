from flask import Flask, jsonify, request
import pymysql

app = Flask(__name__)

@app.route('/getproductsdata', methods=['GET'])
def get_products_data():
    connection = pymysql.connect(host='localhost', user='root', password='Password_123', database='sklepik', cursorclass=pymysql.cursors.DictCursor)

    cursor = connection.cursor()
    cursor.execute("SELECT * FROM products")
    data = cursor.fetchall()
    cursor.close()
    connection.close()
    return jsonify(data)

@app.route('/getusersdata', methods=['POST'])
def get_users_data():
    email = request.args.get('email')
    password = request.args.get('password')

    connection = pymysql.connect(host='localhost', user='root', password='Password_123', database='sklepik', cursorclass=pymysql.cursors.DictCursor)

    cursor = connection.cursor()
    query = "SELECT * FROM users WHERE email = %s AND pass = %s"
    cursor.execute(query, (email, password))
    data = cursor.fetchall()
    cursor.close()
    connection.close()
    return jsonify(data)

@app.route('/placeorder', methods=['POST'])
def place_order():
    order_data = request.get_json()
    cart_items = order_data.get('cartItems')
    user_id = order_data.get('userId')  # Pobranie ID użytkownika z danych zapytania

    connection = pymysql.connect(host='localhost', user='root', password='Password_123', database='sklepik', cursorclass=pymysql.cursors.DictCursor)
    cursor = connection.cursor()

    cursor.execute("SELECT MAX(id_order) FROM orders")
    id_order = cursor.fetchall()

    try:
        cursor.execute(f"INSERT INTO orders (id_order, id_user, status) VALUES ({id_order[0]['MAX(id_order)']}, {user_id}, 'w trakcie')")

        cursor.execute("SELECT MAX(id_order) FROM orders")
        id_order = cursor.fetchall()

        for item in cart_items:
            cursor.execute(f"INSERT INTO details (id_product, ilosc, cena, id_order) VALUES ({item['product']['id']}, {item['quantity']}, {item['product']['cena']}, {id_order[0]['MAX(id_order)']})")

        connection.commit()
    except Exception as e:
        connection.rollback()
        return jsonify({'error': str(e)}), 500
    finally:
        cursor.close()
        connection.close()

    return jsonify({'status': 'success'}), 200

@app.route('/getordersdata', methods=['GET'])
def get_orders_data():
    connection = pymysql.connect(host='localhost', user='root', password='Password_123', database='sklepik', cursorclass=pymysql.cursors.DictCursor)

    cursor = connection.cursor()
    cursor.execute("SELECT orders.id_order, users.imie, users.nazwisko, users.nr_tel, orders.status, products.nazwa AS product_name, details.ilosc AS quantity, SUM(details.ilosc * products.cena) AS total_price FROM orders INNER JOIN users ON orders.id_user = users.id INNER JOIN details ON orders.id_order = details.id_order INNER JOIN products ON details.id_product = products.id_product GROUP BY orders.id_order, users.imie, users.nazwisko, users.nr_tel, orders.status, products.nazwa, details.ilosc;")
    data = cursor.fetchall()
    cursor.close()
    connection.close()
    return jsonify(data)

@app.route('/getuserorder', methods=['GET'])
def get_user_order_data():
    id = request.args.get('id')

    connection = pymysql.connect(host='localhost', user='root', password='Password_123', database='sklepik', cursorclass=pymysql.cursors.DictCursor)

    cursor = connection.cursor()
    cursor.execute("SELECT orders.id_order, users.imie, users.nazwisko, users.nr_tel, orders.status, products.nazwa AS product_name, details.ilosc AS quantity, SUM(details.ilosc * products.cena) AS total_price FROM orders INNER JOIN users ON orders.id_user = users.id INNER JOIN details ON orders.id_order = details.id_order INNER JOIN products ON details.id_product = products.id_product WHERE users.id = %s GROUP BY orders.id_order, users.imie, users.nazwisko, users.nr_tel, orders.status, products.nazwa, details.ilosc;", (id))
    data = cursor.fetchall()
    cursor.close()
    connection.close()
    return jsonify(data)

@app.route('/changeorderstatus', methods=['PUT'])
def change_order_status():
    id_order = request.json.get('id_order')
    new_status = request.json.get('newStatus')

    connection = pymysql.connect(host='localhost', user='root', password='Password_123', database='sklepik', cursorclass=pymysql.cursors.DictCursor)
    cursor = connection.cursor()

    try:
        cursor.execute("UPDATE orders SET status = %s WHERE id_order = %s", (new_status, id_order))
        connection.commit()
    except Exception as e:
        connection.rollback()
        return jsonify({'error': str(e)}), 500
    finally:
        cursor.close()
        connection.close()

    return jsonify({'status': 'success'}), 200

if __name__ == '__main__':
    app.run(host='0.0.0.0', port=5000)
