import random
def generate_data(n):
    val = [random.randint(1, n) for _ in range(n)]  
    wt = [random.randint(1, n) for _ in range(n)]

    # Generating a random value for W
    W = random.randint(n, n*10)  # Generating a random value between 800 and 1200 for W

    # Writing generated data to a text file
    with open(f"data_{n}.txt", 'w') as file:
        file.write(f"{W}\n")
        file.write("" + ", ".join(map(str, val)) + "\n")
        file.write("" + ", ".join(map(str, wt)) + "\n")



generate_data(100)
generate_data(1000)
generate_data(10000)