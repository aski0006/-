import unittest
import os

from main import DataPerHandler


def generate_test_csv():
    test_csv_content = """Name,Age,City
Alice,30,New York
Bob,25,Los Angeles
Charlie,35,Chicago"""

    with open('test_data.csv', 'w', encoding='utf-8') as file:
        file.write(test_csv_content)


class TestDataPerHandler(unittest.TestCase):
    def test_import_csv(self):
        generate_test_csv()
        # 创建DataPerHandler实例并导入CSV文件
        handler = DataPerHandler('test_data.csv', 'output.csv')
        csv_data = handler.import_csv()

        expected_data = [['Name', 'Age', 'City'],
                         ['Alice', '30', 'New York'],
                         ['Bob', '25', 'Los Angeles'],
                         ['Charlie', '35', 'Chicago']]

        self.assertEqual(csv_data, expected_data)
        # 清理临时文件
        os.remove('test_data.csv')

    def test_get_header_column_index(self):
        generate_test_csv()

        handler = DataPerHandler('test_data.csv', 'output.csv')
        handler.import_csv()
        expected_index = handler.get_header_column_index('Name')
        self.assertEqual(expected_index, 0)

        os.remove('test_data.csv')

    def test_handle_name(self):
        generate_test_csv()
        handler = DataPerHandler('test_data.csv', 'output.csv')
        handler.import_csv()
        expected_output = [
            [65, 108, 105, 99, 101, 0, 0],  # "Alice" 补两个 0
            [66, 111, 98, 0, 0, 0, 0],  # "Bob" 补五个 0
            [67, 104, 97, 114, 108, 105, 101]  # "Charlie" 长度正好
        ]
        data = handler.handle_name('Name')
        self.assertEqual(data, expected_output)
        os.remove('test_data.csv')

    def test_handle_nation(self):
        generate_test_csv()
        handler = DataPerHandler('test_data.csv', 'output.csv')
        handler.import_csv()
        data = handler.handle_nation('City')
        expected_output = [0, 1, 2]
        self.assertEqual(data, expected_output)
        os.remove('test_data.csv')

    def test_export_csv(self):
        generate_test_csv()
        handler = DataPerHandler('test_data.csv', 'output.csv')
        handler.import_csv()
        handler.export_csv(["Name", "City"])

        with open('output.csv', 'r', encoding='utf-8') as file:
            csv_data = file.readlines()

        expected_data = ['Name,City\n',
                         '"[65, 108, 105, 99, 101, 0, 0]",0\n',
                         '"[66, 111, 98, 0, 0, 0, 0]",1\n',
                         '"[67, 104, 97, 114, 108, 105, 101]",2\n']

        self.assertEqual(csv_data, expected_data)

        os.remove('test_data.csv')
        os.remove('output.csv')


if __name__ == '__main__':
    unittest.main()
