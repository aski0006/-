import csv

from tqdm import tqdm


class DataPerHandler:
    def __init__(self, _input_file_path, _output_file_path):
        self.input_file_path = _input_file_path
        self.output_file_path = _output_file_path
        self.data = []

    def import_csv(self):
        try:
            with open(self.input_file_path, 'r', encoding='utf-8') as file:
                reader = csv.reader(file)
                total_lines = sum(1 for _ in reader)
                file.seek(0)
                pbar = tqdm(total=total_lines, desc="Importing CSV")
                for row in reader:
                    self.data.append(row)
                    pbar.update(1)
                pbar.close()
            return self.data
        except Exception as e:
            print(f"Error importing CSV file: {e}")

    def get_header_column_index(self, index_str):
        if not self.data:
            return []
        headers = self.data[0]
        try:
            column_index = headers.index(index_str)
            return column_index
        except ValueError:
            print(f"CSV 文件中没有找到 {index_str} 列")

    def handle_name(self, index_str):
        index = self.get_header_column_index(index_str)
        max_length = max(len(row[index]) for row in self.data[1:])
        processed_data = []
        pbar = tqdm(total=len(self.data) - 1, desc="Handle Name")
        for row in self.data[1:]:
            name_letters = list(row[index])
            name_ascii_codes = [ord(letter) for letter in name_letters]
            while len(name_ascii_codes) < max_length:
                name_ascii_codes.append(0)
            processed_data.append(name_ascii_codes)
            pbar.update(1)
        pbar.close()
        return processed_data

    def handle_nation(self, index_str):
        index = self.get_header_column_index(index_str)
        dict_ = {}
        current_id = 0
        labels_ = []
        pbar = tqdm(total=len(self.data) - 1, desc="Handle Nation")
        for row in self.data[1:]:
            set_id = row[index]
            if set_id not in dict_:
                dict_[set_id] = current_id
                current_id += 1
            labels_.append(dict_[set_id])
            pbar.update(1)
        pbar.close()
        return labels_

    def export_csv(self, columns):
        inputs = self.handle_name(columns[0])
        labels = self.handle_nation(columns[1])

        if len(inputs) != len(labels):
            raise ValueError("Inputs and labels must have the same length.")

        with open(self.output_file_path, 'w', newline='', encoding='utf-8') as file:
            writer = csv.writer(file)
            writer.writerow(columns)
            pbar = tqdm(total=len(self.data) - 1, desc="Export CSV")
            for input_row, label in zip(inputs, labels):
                writer.writerow([input_row, label])
                pbar.update(1)
            pbar.close()


if __name__ == "__main__":
    input_file_path = "names_countries.csv"
    output_file_path = "output.csv"
    handler = DataPerHandler(input_file_path, output_file_path)
    handler.import_csv()
    handler.export_csv(["Name","Nation"])

