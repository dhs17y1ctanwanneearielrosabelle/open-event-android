package org.fossasia.openevent.viewmodels;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Transformations;
import android.arch.lifecycle.ViewModel;

import org.fossasia.openevent.data.Microlocation;
import org.fossasia.openevent.dbutils.FilterableRealmLiveData;
import org.fossasia.openevent.dbutils.RealmDataRepository;

import java.util.List;
import java.util.Locale;

import io.reactivex.functions.Predicate;

public class LocationsFragmentViewModel extends ViewModel {

    private FilterableRealmLiveData<Microlocation> filterableRealmLiveData;
    private LiveData<List<Microlocation>> filteredLocations;
    private String searchText = "";

    public LocationsFragmentViewModel() {
        filterableRealmLiveData = RealmDataRepository.asFilterableLiveData(RealmDataRepository.getDefaultInstance().getLocations());
    }

    public LiveData<List<Microlocation>> getLocations(String searchText) {
        if (!this.searchText.equals(searchText) || filteredLocations == null) {
            setSearchText(searchText);
            final String query = searchText.toLowerCase(Locale.getDefault());
            Predicate<Microlocation> predicate = location -> location.getName()
                    .toLowerCase(Locale.getDefault())
                    .contains(query);
            filterableRealmLiveData.filter(predicate);
            if (filteredLocations == null) {
                filteredLocations = Transformations.map(filterableRealmLiveData, input -> input);
            }
        }
        return filteredLocations;
    }

    public String getSearchText() {
        return searchText;
    }

    public void setSearchText(String searchText) {
        this.searchText = searchText;
    }
}
