package one.marcomass.ezeat.fragments;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.squareup.picasso.Picasso;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.core.widget.ContentLoadingProgressBar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SimpleItemAnimator;
import one.marcomass.ezeat.activities.MainActivity;
import one.marcomass.ezeat.db.entity.DishEntity;
import one.marcomass.ezeat.models.Menu;
import one.marcomass.ezeat.viewmodels.MainViewModel;
import one.marcomass.ezeat.R;
import one.marcomass.ezeat.Util;
import one.marcomass.ezeat.adapaters.MenuAdapter;
import one.marcomass.ezeat.viewmodels.OrderViewModel;

public class MenuFragment extends Fragment implements MenuAdapter.OrderManager {

    private RecyclerView recyclerMenu;
    private ActionBar toolbar;
    private MenuAdapter menuAdapter;
    private LinearLayoutManager linearLayoutManager;
    private ContentLoadingProgressBar loadingProgressBar;
    private String restaurantID;

    private ImageView imageLogo;
    private TextView textName;
    private TextView textDescription;
    private TextView textAddress;
    private TextView textPhoneNumber;
    private TextView textMinOrder;
    private RatingBar ratingBar;

    private boolean loaded = false;


    private MainViewModel mainViewModel;
    private OrderViewModel orderViewModel;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainViewModel = ViewModelProviders.of(getActivity()).get(MainViewModel.class);
        orderViewModel = ViewModelProviders.of(getActivity()).get(OrderViewModel.class);
        setHasOptionsMenu(false);

        if (loaded) {
            mainViewModel.setBottomSheetState(BottomSheetBehavior.STATE_COLLAPSED);
        }

        if (getArguments() != null) {
            restaurantID = getArguments().getString(Util.RESTAURANT_ID, "noID");
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_menu, container, false);

        toolbar = ((MainActivity) getActivity()).getSupportActionBar();
        toolbar.setTitle("");
        toolbar.setDisplayHomeAsUpEnabled(true);

        getActivity().findViewById(R.id.toolbar).setBackground(getResources().getDrawable(R.drawable.gradient_toolbar));

        imageLogo = rootView.findViewById(R.id.image_restaurant_logo);
        //textName = rootView.findViewById(R.id.text_restaurant_name);
        textDescription = rootView.findViewById(R.id.text_restaurant_description);
        textAddress = rootView.findViewById(R.id.text_restaurant_address);
        textPhoneNumber = rootView.findViewById(R.id.text_restaurant_phone_number);
        textMinOrder = rootView.findViewById(R.id.text_restaurant_min_order);
        ratingBar = rootView.findViewById(R.id.rating_restaurant);

        loadingProgressBar = rootView.findViewById(R.id.progress_menu);
        recyclerMenu = rootView.findViewById(R.id.recycler_menu);
        linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerMenu.setLayoutManager(linearLayoutManager);
        ((SimpleItemAnimator) recyclerMenu.getItemAnimator()).setSupportsChangeAnimations(false);
        menuAdapter = new MenuAdapter(null, this);
        recyclerMenu.setAdapter(menuAdapter);

        final MenuFragment lifecycle = this;

        mainViewModel.getRestaurantMenu(restaurantID).observe(this, new Observer<Menu>() {
            @Override
            public void onChanged(final Menu menu) {
                if (menu.getProducts() != null) {
                    for (int i = 0; i < menu.getProducts().size(); i++) {
                        final int pos = i;
                        menu.getProducts().get(pos).setLiveQuantity(orderViewModel).observe(lifecycle, new Observer<Integer>() {
                            @Override
                            public void onChanged(Integer integer) {
                                menu.getProducts().get(pos).setQuantity(integer == null ? 0 : integer);
                                menuAdapter.notifyItemChanged(pos);
                            }
                        });
                    }
                    bindRestaurantData(menu);
                    menuAdapter.setDataSet(menu.getProducts());
                    loaded = true;
                    mainViewModel.setBottomSheetState(BottomSheetBehavior.STATE_COLLAPSED);
                    orderViewModel.setMinimum(menu.getMinOrder());
                }
            }
        });
        return rootView;
    }

    @Override
    public void onPause() {
        mainViewModel.setBottomSheetState(BottomSheetBehavior.STATE_HIDDEN);
        super.onPause();
    }

    @Override
    public void onResume() {
        if (loaded) {
            mainViewModel.setBottomSheetState(BottomSheetBehavior.STATE_COLLAPSED);
        }
        super.onResume();
    }

    @Override
    public void addDish(final DishEntity dish) {
        if (!orderViewModel.addDish(dish)) {
            new AlertDialog.Builder(getContext(), R.style.ThemeOverlay_MaterialComponents_Dialog_Alert)
                    .setTitle("Hai già un ordine")
                    .setMessage("Puoi ordinare da un unico ristorante contemporaneamente. \nVuoi eliminare l'ordine precedente?")
                    .setPositiveButton("Elimina", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            orderViewModel.removeAll();
                            //TODO check thread concurrency
                            orderViewModel.addDish(dish);
                        }
                    })
                    .setNegativeButton("Annulla", null)
                    .show();
        }
    }

    @Override
    public void removeDish(String dishID) {
        orderViewModel.removeDish(dishID);
    }

    public void bindRestaurantData(Menu menu) {
        //textName.setText(menu.getName());
        textDescription.setText(menu.getDescription());
        textAddress.setText(menu.getAddress());
        textPhoneNumber.setText(menu.getPhoneNumber());
        textMinOrder.setText("Ordine minimo: " + String.format("%.2f", menu.getMinOrder()) + "€");
        ratingBar.setRating(menu.getRating());
        Picasso.get().load(menu.getImage_url()).into(imageLogo);
        toolbar.setTitle(menu.getName());
    }
}

