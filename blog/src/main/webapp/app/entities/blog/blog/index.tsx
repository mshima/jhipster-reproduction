import React from 'react';
import { Route } from 'react-router';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import Blog from './blog';
import BlogDeleteDialog from './blog-delete-dialog';
import BlogDetail from './blog-detail';
import BlogUpdate from './blog-update';

const BlogRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<Blog />} />
    <Route path="new" element={<BlogUpdate />} />
    <Route path=":id">
      <Route index element={<BlogDetail />} />
      <Route path="edit" element={<BlogUpdate />} />
      <Route path="delete" element={<BlogDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default BlogRoutes;
