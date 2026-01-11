class MediaItem:
    def __init__(self, title, category, year, rating=None, tags=None):
        if not isinstance(title, str) or title.strip() == "":
            raise ValueError("Title must be a non-empty string.")

        if category not in {"book", "movie", "game", "tv show", "podcast"}:
            raise ValueError("Invalid category.")

        if rating is not None and not (0 <= rating <= 5):
            raise ValueError("Rating must be between 0 and 5.")

        self.title = title
        self.category = category
        self.year = year
        self.rating = rating
        self.tags = tags or []


class MediaCatalogue:
    def __init__(self):
        self.items = []

    def add(self, item):
        if any(i.title == item.title for i in self.items):
            raise ValueError("Duplicate title.")
        self.items.append(item)

    def remove(self, title):
        for item in self.items:
            if item.title == title:
                self.items.remove(item)
                return item
        return None

    def search(self, keyword):
        keyword = keyword.lower()
        return [
            i for i in self.items
            if keyword in i.title.lower()
               or keyword in i.category.lower()
        ]

    def search_by_tag(self, tag):
        tag = tag.lower()
        return [
            i for i in self.items
            if tag in [t.lower() for t in i.tags]
        ]

    def filter_by_year(self, start, end):
        return [i for i in self.items if start <= i.year <= end]

    def sorted_by_year(self, descending=False):
        return sorted(self.items, key=lambda i: i.year, reverse=descending)

    def update_year(self, title, new_year):
        for item in self.items:
            if item.title == title:
                item.year = new_year
                return True
        return False
    
    def recommend_similar(self, item):
        return [
            i for i in self.items
            if i.category == item.category and i.title != item.title
        ]

    def recommend_recent(self, year_threshold):
        return [
            i for i in self.items
            if i.year >= year_threshold
        ]
