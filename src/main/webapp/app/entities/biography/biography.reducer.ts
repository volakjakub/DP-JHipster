import axios from 'axios';
import { createAsyncThunk, isFulfilled, isPending } from '@reduxjs/toolkit';
import { cleanEntity } from 'app/shared/util/entity-utils';
import { EntityState, IQueryParams, createEntitySlice, serializeAxiosError } from 'app/shared/reducers/reducer.utils';
import { IBiography, defaultValue } from 'app/shared/model/biography.model';

const initialState: EntityState<IBiography> = {
  loading: false,
  errorMessage: null,
  entities: [],
  entity: defaultValue,
  updating: false,
  totalItems: 0,
  updateSuccess: false,
};

const apiUrl = 'api/biographies';

// Actions

export const getEntities = createAsyncThunk(
  'biography/fetch_entity_list',
  async ({ page, size, sort }: IQueryParams) => {
    const requestUrl = `${apiUrl}?${sort ? `page=${page}&size=${size}&sort=${sort}&` : ''}cacheBuster=${new Date().getTime()}`;
    return axios.get<IBiography[]>(requestUrl);
  },
  { serializeError: serializeAxiosError },
);

export const getEntity = createAsyncThunk(
  'biography/fetch_entity',
  async (id: string | number) => {
    const requestUrl = `${apiUrl}/${id}`;
    return axios.get<IBiography>(requestUrl);
  },
  { serializeError: serializeAxiosError },
);

export const getBiographyEntityByUsername = createAsyncThunk(
  'biography/fetch_entity_by_username',
  async (id: string | number, { rejectWithValue }) => {
    try {
      const requestUrl = `${apiUrl}/user?username=${id}`;
      return axios.get<IBiography>(requestUrl);
    } catch (error) {
      if (error.response?.status === 404) {
        return null; // Instead of rejecting, return null
      }
      return rejectWithValue(error.message);
    }
  },
  { serializeError: serializeAxiosError },
);

export const createEntity = createAsyncThunk(
  'biography/create_entity',
  async (entity: IBiography, thunkAPI) => {
    const result = await axios.post<IBiography>(apiUrl, cleanEntity(entity));
    return result;
  },
  { serializeError: serializeAxiosError },
);

export const updateEntity = createAsyncThunk(
  'biography/update_entity',
  async (entity: IBiography, thunkAPI) => {
    const result = await axios.put<IBiography>(`${apiUrl}/${entity.id}`, cleanEntity(entity));
    return result;
  },
  { serializeError: serializeAxiosError },
);

export const partialUpdateEntity = createAsyncThunk(
  'biography/partial_update_entity',
  async (entity: IBiography, thunkAPI) => {
    const result = await axios.patch<IBiography>(`${apiUrl}/${entity.id}`, cleanEntity(entity));
    return result;
  },
  { serializeError: serializeAxiosError },
);

export const deleteEntity = createAsyncThunk(
  'biography/delete_entity',
  async (id: string | number, thunkAPI) => {
    const requestUrl = `${apiUrl}/${id}`;
    const result = await axios.delete<IBiography>(requestUrl);
    return result;
  },
  { serializeError: serializeAxiosError },
);

// slice

export const BiographySlice = createEntitySlice({
  name: 'biography',
  initialState,
  extraReducers(builder) {
    builder
      .addCase(getEntity.fulfilled, (state, action) => {
        state.loading = false;
        state.entity = action.payload.data;
      })
      .addCase(getBiographyEntityByUsername.fulfilled, (state, action) => {
        state.loading = false;
        state.entity = action.payload?.data;
      })
      .addCase(deleteEntity.fulfilled, state => {
        state.updating = false;
        state.updateSuccess = true;
        state.entity = {};
      })
      .addMatcher(isFulfilled(getEntities), (state, action) => {
        const { data, headers } = action.payload;

        return {
          ...state,
          loading: false,
          entities: data,
          totalItems: parseInt(headers['x-total-count'], 10),
        };
      })
      .addMatcher(isFulfilled(createEntity, updateEntity, partialUpdateEntity), (state, action) => {
        state.updating = false;
        state.loading = false;
        state.updateSuccess = true;
        state.entity = action.payload.data;
      })
      .addMatcher(isPending(getEntities, getEntity), state => {
        state.errorMessage = null;
        state.updateSuccess = false;
        state.loading = true;
      })
      .addMatcher(isPending(createEntity, updateEntity, partialUpdateEntity, deleteEntity), state => {
        state.errorMessage = null;
        state.updateSuccess = false;
        state.updating = true;
      });
  },
});

export const { reset } = BiographySlice.actions;

// Reducer
export default BiographySlice.reducer;
