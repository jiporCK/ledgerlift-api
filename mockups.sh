
echo "Creating user..."
http POST :8080/api/v1/users phoneNumber="0987654321" email="srengchipor99@gmail.com" username="chipor" password="passwordpw"

echo "Creating category..."
http POST :8080/api/v1/categories name="Medical" description="medical"