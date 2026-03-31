import pytest
import time
from selenium import webdriver
from selenium.webdriver.common.by import By
from selenium.webdriver.common.keys import Keys
from selenium.webdriver.support.ui import WebDriverWait
from selenium.webdriver.support import expected_conditions as EC
from selenium.webdriver.common.action_chains import ActionChains
from selenium.webdriver.support.ui import WebDriverWait
from selenium.webdriver.support import expected_conditions as EC
from selenium.webdriver.common.by import By

# Run using the command: pytest test/test_suite.py -s
class TestUniversitySystem:
    def setup_method(self):
        self.driver = webdriver.Firefox()
        self.driver.maximize_window()

    def teardown_method(self):
        self.driver.quit()

    def test_login(self):
        self.driver.get("http://localhost/3S03-T5/login.php")
        time.sleep(1)
        self.driver.find_element(By.NAME, "username").send_keys("test")
        self.driver.find_element(By.NAME, "password").send_keys("test")
        time.sleep(1)

        # Submit login form
        self.driver.find_element(By.TAG_NAME, "button").click()
        time.sleep(1)

        # Wait for redirection and check URL
        WebDriverWait(self.driver, 5).until(EC.url_contains("index.php"))
        assert "index.php" in self.driver.current_url, "Failed to log in!"

    def test_index_page(self):
        self.test_login()
        time.sleep(3)

        # Verify navigation links exist
        links = ["Main Page", "Hidden Content"]
        for link in links:
            assert self.driver.find_element(By.LINK_TEXT, link).is_displayed(), f"{link} not found!"

        # Check each preview works
        previews = self.driver.find_elements(By.CLASS_NAME, "preview-box")
        for box in previews:
            button = box.find_element(By.TAG_NAME, "a")  # Find the button inside the box
            img = box.find_element(By.CLASS_NAME, "preview")
            initial_src = img.get_attribute("src")

            # Scroll the button into view
            self.driver.execute_script("arguments[0].scrollIntoView({block: 'center'});", button)

            # Hover over the button
            hover = ActionChains(self.driver).move_to_element(button)
            hover.perform()

            # Wait until the GIF starts playing (src changes)
            WebDriverWait(self.driver, 10).until(
                lambda d: img.get_attribute("src") != initial_src
            )
            time.sleep(2)

            assert img.get_attribute("src") != initial_src, "Preview GIF did not start!"

    def test_main_page(self):
        self.test_login()
        self.driver.find_element(By.LINK_TEXT, "Main Page").click()
        time.sleep(3)  # Allow user to observe the transition

        # Ensure table exists
        table = self.driver.find_element(By.ID, "infoTable")
        assert table.is_displayed(), "Main data table is missing!"

        # Fill in new entry
        name = "Jackson Field"
        age = "22"
        department = "Engineering"
        gpa = "9.8"

        self.driver.find_element(By.ID, "name").send_keys(name)
        time.sleep(1)
        self.driver.find_element(By.ID, "age").send_keys(age)
        time.sleep(1)
        self.driver.find_element(By.ID, "department").send_keys(department)
        time.sleep(1)
        self.driver.find_element(By.ID, "gpa").send_keys(gpa)
        time.sleep(1)
        self.driver.find_element(By.ID, "addEntry").click()

        time.sleep(2)

        # Use the search box to find newly added entry
        search_box = self.driver.find_element(By.CSS_SELECTOR, "input[type='search']")
        search_box.send_keys(name)
        time.sleep(2)  # Wait for table to filter

        # Verify new entry appears in filtered results
        rows = self.driver.find_elements(By.CSS_SELECTOR, "#infoTable tbody tr")
        assert any(name in row.text for row in rows), "New entry was not found after searching!"

    def test_hidden_page(self):
        self.test_login()
        self.driver.find_element(By.LINK_TEXT, "Hidden Content").click()

        # Wait until the loading screen disappears
        WebDriverWait(self.driver, 10).until(
            EC.invisibility_of_element_located((By.ID, "loadingScreen"))
        )

        # Wait for the chart to load
        WebDriverWait(self.driver, 10).until(
            EC.visibility_of_element_located((By.ID, "lineChart"))
        )
        assert self.driver.find_element(By.ID, "lineChart").is_displayed(), "Chart is missing!"

        # Wait until at least 10 history entries appear
        WebDriverWait(self.driver, 15).until(
            lambda d: len(d.find_elements(By.CSS_SELECTOR, "#historyContent .history-item")) >= 10
        )

        # Check if history section is now visible
        history_section = self.driver.find_element(By.ID, "history")
        assert history_section.is_displayed(), "History section is missing despite >= 5 updates!"

        # Wait for the data panel to update with the latest request
        WebDriverWait(self.driver, 10).until(
            lambda d: d.find_element(By.ID, "latestTime").text != "0:00:00 AM"
        )

        # Verify the latest request data
        latest_time = self.driver.find_element(By.ID, "latestTime").text
        latest_requests = self.driver.find_element(By.ID, "latestRequests").text
        assert latest_time != "0:00:00 AM", "Latest time should be updated!"
        assert int(latest_requests) > 0, "Request count should be greater than 0!"