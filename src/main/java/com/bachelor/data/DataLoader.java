package com.bachelor.data;

import com.bachelor.data.json.LoadResult;
import com.bachelor.exceptions.DataLoadException;

public interface DataLoader {
    LoadResult load() throws DataLoadException;
}