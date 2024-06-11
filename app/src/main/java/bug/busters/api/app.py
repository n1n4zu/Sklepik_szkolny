# API na serwerze

from flask import Flask, jsonify, request
import pymysql, os

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
    user_id = order_data.get('userId')

    connection = pymysql.connect(host='localhost', user='root', password='Password_123', database='sklepik', cursorclass=pymysql.cursors.DictCursor)
    cursor = connection.cursor()

    try:
        for item in cart_items:
            cursor.execute("SELECT ilosc FROM products WHERE id_product = %s", (item['product']['id_product'],))
            product = cursor.fetchone()
            if not product or product['ilosc'] < item['quantity']:
                return jsonify({'error': f"Produkt {item['product']['name']} nie jest dostępny w wystarczającej ilości."}), 400

        cursor.execute("SELECT MAX(id_order) AS max_id_order FROM orders")
        result = cursor.fetchone()
        id_order = (result['max_id_order'] or 0) + 1

        cursor.execute("INSERT INTO orders (id_order, id_user, status) VALUES (%s, %s, 'w trakcie')", (id_order, user_id))

        for item in cart_items:
            cursor.execute("INSERT INTO details (id_product, ilosc, cena, id_order) VALUES (%s, %s, %s, %s)",
                           (item['product']['id_product'], item['quantity'], item['product']['cena'], id_order))
            cursor.execute("UPDATE products SET ilosc = ilosc - %s WHERE id_product = %s",
                           (item['quantity'], item['product']['id_product']))

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

@app.route('/editproduct', methods=['PUT'])
def edit_product():
    product_data = request.get_json()
    product_id = product_data.get('id_product')
    new_name = product_data.get('name')
    new_price = product_data.get('price')
    new_quantity = product_data.get('quantity')

    connection = pymysql.connect(host='localhost', user='root', password='Password_123', database='sklepik', cursorclass=pymysql.cursors.DictCursor)
    cursor = connection.cursor()

    try:
        cursor.execute("UPDATE products SET nazwa = %s, cena = %s, ilosc = %s WHERE id_product = %s",
                       (new_name, new_price, new_quantity, product_id))
        connection.commit()
    except Exception as e:
        connection.rollback()
        return jsonify({'error': str(e)}), 500
    finally:
        cursor.close()
        connection.close()

    return jsonify({'status': 'success'}), 200

@app.route('/getimagelist', methods=['GET'])
def get_image_list():
    images_folder = '/var/www/html/images'
    try:
        images = os.listdir(images_folder)
        return jsonify(images)
    except Exception as e:
        return jsonify({'error': str(e)}), 500

@app.route('/addproduct', methods=['POST'])
def add_product():
    product_data = request.get_json()
    name = product_data.get('nazwa')
    quantity = product_data.get('ilosc')
    price = product_data.get('cena')
    image = product_data.get('obraz')

    # Validate the received data
    if not name or not isinstance(quantity, int) or not isinstance(price, (int, float)) or not image:
        return jsonify({'error': 'Invalid input data'}), 400

    connection = pymysql.connect(host='localhost', user='root', password='Password_123', database='sklepik', cursorclass=pymysql.cursors.DictCursor)
    cursor = connection.cursor()

    try:
        cursor.execute("INSERT INTO products (nazwa, ilosc, cena, obraz) VALUES (%s, %s, %s, %s)", (name, quantity, price, image))
        connection.commit()
    except Exception as e:
        connection.rollback()
        return jsonify({'error': str(e)}), 500
    finally:
        cursor.close()
        connection.close()

    return jsonify({'status': 'success'}), 200

@app.route('/uploadimage', methods=['POST'])
def upload_image():
    if 'image' not in request.files:
        return jsonify({'error': 'No image provided'}), 400

    image = request.files['image']
    if image.filename == '':
        return jsonify({'error': 'No image selected'}), 400

    # Zapisujemy plik obrazu do folderu
    image.save(os.path.join('/var/www/html/images', image.filename))
    return jsonify([image.filename]), 200

if __name__ == '__main__':
    app.run(host='0.0.0.0', port=5000)
