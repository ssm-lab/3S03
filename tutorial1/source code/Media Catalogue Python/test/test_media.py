import pytest
from media import MediaCatalogue, MediaItem
# Run test using the command: pytest

@pytest.fixture
def sample_catalogue():
    cat = MediaCatalogue()
    cat.add(MediaItem("Dune", "book", 1965, rating=5, tags=["sci-fi"]))
    cat.add(MediaItem("Dune (Movie)", "movie", 2021, rating=4, tags=["sci-fi", "remake"]))
    cat.add(MediaItem("Fallout", "game", 1997, rating=5, tags=["post-apocalyptic"]))
    cat.add(MediaItem("Resident Evil", "game", 1996, rating=4, tags=["horror", "survival"]))
    return cat


def test_mediaitem_valid():
    item = MediaItem("The Sims 3", "game", 2009, rating=5, tags=["simulation"])
    assert item.title == "The Sims 3"
    assert item.category == "game"
    assert item.year == 2009
    assert item.rating == 5
    assert item.tags == ["simulation"]


@pytest.mark.parametrize("title", ["", "   ", None])
def test_mediaitem_invalid_title(title):
    with pytest.raises(ValueError):
        MediaItem(title, "book", 2000)

@pytest.mark.parametrize("category", ["music", "film", "novel"])
def test_mediaitem_invalid_category(category):
    with pytest.raises(ValueError):
        MediaItem("Some Title", category, 1999)

@pytest.mark.parametrize("rating", [-1, 6, 10])
def test_mediaitem_invalid_rating(rating):
    with pytest.raises(ValueError):
        MediaItem("Valid", "book", 2000, rating=rating)


def test_add_duplicate_title(sample_catalogue):
    with pytest.raises(ValueError):
        sample_catalogue.add(MediaItem("Dune", "book", 2020))

def test_remove_existing(sample_catalogue):
    removed = sample_catalogue.remove("Dune")
    assert removed.title == "Dune"
    assert len(sample_catalogue.items) == 3

def test_remove_nonexistent(sample_catalogue):
    removed = sample_catalogue.remove("Never Existed")
    assert removed is None
    assert len(sample_catalogue.items) == 3


def test_search_title(sample_catalogue):
    results = sample_catalogue.search("dune")
    assert len(results) == 2   # book + movie

def test_search_category(sample_catalogue):
    results = sample_catalogue.search("game")
    titles = [i.title for i in results]
    assert len(results) == 2
    assert "Fallout" in titles
    assert "Resident Evil" in titles


@pytest.mark.parametrize("tag, expected", [
    ("sci-fi", 2),
    ("remake", 1),
    ("post-apocalyptic", 1),
    ("horror", 1),
    ("unknown", 0),
])
def test_search_by_tag(sample_catalogue, tag, expected):
    assert len(sample_catalogue.search_by_tag(tag)) == expected


def test_filter_by_year(sample_catalogue):
    results = sample_catalogue.filter_by_year(1990, 2023)
    assert len(results) == 3


def test_sorted_by_year_ascending(sample_catalogue):
    sorted_items = sample_catalogue.sorted_by_year()
    years = [i.year for i in sorted_items]
    assert years == sorted(years)

def test_sorted_by_year_desc(sample_catalogue):
    sorted_items = sample_catalogue.sorted_by_year(descending=True)
    years = [i.year for i in sorted_items]
    assert years == sorted(years, reverse=True)

def test_update_year_success(sample_catalogue):
    assert sample_catalogue.update_year("Dune", 1970)
    dune = next(i for i in sample_catalogue.items if i.title == "Dune")
    assert dune.year == 1970

def test_update_year_failure(sample_catalogue):
    assert not sample_catalogue.update_year("Unknown", 2000)



def test_recommend_similar(sample_catalogue):
    fallout = next(i for i in sample_catalogue.items if i.title == "Fallout")
    recs = sample_catalogue.recommend_similar(fallout)
    titles = [i.title for i in recs]

    assert "Resident Evil" in titles
    assert "Fallout" not in titles

def test_recommend_recent(sample_catalogue):
    recs = sample_catalogue.recommend_recent(2000)
    titles = [i.title for i in recs]

    assert "Dune (Movie)" in titles     
    assert "Fallout" not in titles      
    assert "Resident Evil" not in titles  
