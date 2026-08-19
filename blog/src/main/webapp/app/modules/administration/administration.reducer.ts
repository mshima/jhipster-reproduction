import axios from 'axios';
import { createAsyncThunk, createSlice, isPending, isRejected } from '@reduxjs/toolkit';

import { serializeAxiosError } from 'app/shared/reducers/reducer.utils';
import { AppThunk } from 'app/config/store';

const initialState = {
  loading: false,
  errorMessage: null as string | null,
  totalItems: 0,
};

export type AdministrationState = Readonly<typeof initialState>;

// Actions

export const AdministrationSlice = createSlice({
  name: 'administration',
  initialState,
  reducers: {},
  extraReducers() {},
});

// Reducer
export default AdministrationSlice.reducer;
