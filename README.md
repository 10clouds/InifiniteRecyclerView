# InifiniteRecyclerView
This is a tiny library for implementing endless loading list easily in Android applications, similar to those seen in Facebook or Twitter apps. It consists of LoadingRecyclerView and AbstractLoadingAdapter that has to be extended by user for usage in their project.

![Demo](http://i.giphy.com/l3q2WWM6bhe7kwbmw.gif)

# Usage
To use this library, you will need to implement AbstractEndlessAdapter, ItemsLoader interface, and use them with EndlessRecyclerView.

# ItemsLoader implementation

ItemsLoader interface consists of one method - getNewItems(). This method provides new items to add to the list whenever the user scrolls to the bottom of the recyclerview. In the provided example, implementation of this interface looks as following:

```java
public class SearchCardsLoader implements ItemsLoader<Card> {
    private static final int PAGE_SIZE = 20;
    private int pageNo = 1;
    private final String searchQuery;
    private ShowErrorInterface showErrorInterface;

    public SearchCardsLoader(@Nullable String searchQuery, ShowErrorInterface showErrorInterface) {
        this.searchQuery = searchQuery;
        this.showErrorInterface = showErrorInterface;
    }

    @Override
    public List<Card> getNewItems() {
        ArrayList<String> filter = new ArrayList<>();
        filter.add("pageSize=" + PAGE_SIZE);
        filter.add("page=" + pageNo);

        if (searchQuery != null && !searchQuery.isEmpty()) {
            filter.add("name=" + searchQuery);
        }

        try {
            List<Card> cardsPage = CardAPI.getAllCards(filter);
            ++pageNo;
            return cardsPage;
        } catch (Exception e){
            e.printStackTrace();
            showErrorInterface.showError(e.getLocalizedMessage());
            return null;
        }
    }

    public interface ShowErrorInterface {
        void showError(String errorText);
    }
}
```

# AbstractEndlessAdapter

The implementation of AbstractEndlessAdapter might look something like this:

```java
public class MtgCardsEndlessAdapter extends AbstractEndlessAdapter<Card> {
    private ItemSelectedListener itemSelectedListener;

    public MtgCardsEndlessAdapter(Context context, AbstractItemsLoader<Card> itemsLoader, ItemSelectedListener itemSelectedListener) {
        super(context, R.layout.view_empty, itemsLoader);
        this.itemSelectedListener = itemSelectedListener;
    }

    @Override
    protected long getRecyclerItemId(int position) {
        return 0;
    }

    @Override
    protected RecyclerView.ViewHolder createRecyclerItemViewHolder(ViewGroup parent, int viewType) {
        ViewCardBinding binding = DataBindingUtil.inflate(getInflater(), R.layout.view_card, parent, false);
        return new CardViewHolder(binding);
    }

    @Override
    protected void bindRecyclerViewHolder(RecyclerView.ViewHolder holder, int position) {
        final Card item = getItem(position);
        CardViewHolder cardViewHolder = (CardViewHolder) holder;
        cardViewHolder.setCard(item);
        if (itemSelectedListener != null) {
            cardViewHolder.binding.getRoot().setOnClickListener((View view) -> itemSelectedListener.onItemSelected(item));
        }
    }

    @Override
    protected int getRecyclerItemViewType(int position) {
        return 0;
    }

    private class CardViewHolder extends RecyclerView.ViewHolder {
        private ViewCardBinding binding;

        public CardViewHolder(ViewCardBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void setCard(Card card) {
            binding.setCard(card);
        }
    }

    public interface ItemSelectedListener {
        void onItemSelected(Card card);
    }
}
```

You can use multiple view types if you want to, just like in "normal" adapter.

# EndlessRecyclerView
This class simply needs to be used instead of normal recycler view, to send callbacks to the adapter when the view is scrolled to the bottom.
