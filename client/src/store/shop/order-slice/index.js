import { createAsyncThunk, createSlice } from "@reduxjs/toolkit";
import axios from "axios";

const initialState = {
  approvalURL: null,
  isLoading: false,
  orderId: null,
  orderList: [],
  orderDetails: null,
};

// --- Create new order (PayPal init)
export const createNewOrder = createAsyncThunk(
  "/order/createNewOrder",
  async (orderData) => {
    const response = await axios.post(
      "http://localhost:5000/api/shop/order/create",
      orderData
    );
    return response.data; // { success: true, data: { approvalURL, orderId } }
  }
);

// --- Capture PayPal payment
export const capturePayment = createAsyncThunk(
  "/order/capturePayment",
  async ({ paymentId, payerId }) => {
    const orderId = JSON.parse(sessionStorage.getItem("currentOrderId"));
    const response = await axios.post(
      "http://localhost:5000/api/shop/order/capture",
      { paymentId, payerId, orderId }
    );
    return response.data; // { success: true/false, data: { ... } }
  }
);

// --- Fetch orders by user
export const getAllOrdersByUserId = createAsyncThunk(
  "/order/getAllOrdersByUserId",
  async (userId) => {
    const response = await axios.get(
      `http://localhost:5000/api/shop/order/list/${userId}`
    );
    return response.data; // { success: true, data: [...] }
  }
);

// --- Fetch single order details
export const getOrderDetails = createAsyncThunk(
  "/order/getOrderDetails",
  async (id) => {
    const response = await axios.get(
      `http://localhost:5000/api/shop/order/details/${id}`
    );
    return response.data; // { success: true, data: {...} }
  }
);

const shoppingOrderSlice = createSlice({
  name: "shoppingOrderSlice",
  initialState,
  reducers: {
    resetOrderDetails: (state) => {
      state.orderDetails = null;
    },
  },
  extraReducers: (builder) => {
    builder
      // --- Create Order ---
      .addCase(createNewOrder.pending, (state) => {
        state.isLoading = true;
      })
      .addCase(createNewOrder.fulfilled, (state, action) => {
        state.isLoading = false;
        if (action.payload?.success) {
          state.approvalURL = action.payload.data.approvalURL;
          state.orderId = action.payload.data.orderId;

          // Save orderId for capture step
          sessionStorage.setItem(
            "currentOrderId",
            JSON.stringify(action.payload.data.orderId)
          );
        } else {
          state.approvalURL = null;
          state.orderId = null;
        }
      })
      .addCase(createNewOrder.rejected, (state) => {
        state.isLoading = false;
        state.approvalURL = null;
        state.orderId = null;
      })

      // --- Capture Payment ---
      .addCase(capturePayment.pending, (state) => {
        state.isLoading = true;
      })
      .addCase(capturePayment.fulfilled, (state, action) => {
        state.isLoading = false;
        if (action.payload?.success) {
          state.orderDetails = action.payload.data;
          state.approvalURL = null;
          state.orderId = null;

          // ✅ clear cart on successful payment
          localStorage.removeItem("cartItems");
        }
      })
      .addCase(capturePayment.rejected, (state) => {
        state.isLoading = false;
      })

      // --- Get All Orders by User ---
      .addCase(getAllOrdersByUserId.pending, (state) => {
        state.isLoading = true;
      })
      .addCase(getAllOrdersByUserId.fulfilled, (state, action) => {
        state.isLoading = false;
        state.orderList =
          action.payload?.data?.map((o) => ({
            ...o,
            orderStatus: o.orderStatus?.toLowerCase(),
            paymentStatus: o.paymentStatus?.toLowerCase(),
          })) || [];
      })
      .addCase(getAllOrdersByUserId.rejected, (state) => {
        state.isLoading = false;
        state.orderList = [];
      })

      // --- Get Order Details ---
      .addCase(getOrderDetails.pending, (state) => {
        state.isLoading = true;
      })
      .addCase(getOrderDetails.fulfilled, (state, action) => {
        state.isLoading = false;
        state.orderDetails = action.payload?.data || null;
      })
      .addCase(getOrderDetails.rejected, (state) => {
        state.isLoading = false;
        state.orderDetails = null;
      });
  },
});

export const { resetOrderDetails } = shoppingOrderSlice.actions;
export default shoppingOrderSlice.reducer;
