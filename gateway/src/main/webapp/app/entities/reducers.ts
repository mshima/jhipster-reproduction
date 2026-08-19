import product from 'app/entities/store/product/product.reducer';
import userData from 'app/entities/user-data/user-data.reducer';
/* jhipster-needle-add-reducer-import - JHipster will add reducer here */

const entitiesReducers = {
  userData,
  product,
  // jhipster-needle-add-reducer-combine - JHipster will add reducer here
};

export default entitiesReducers;
