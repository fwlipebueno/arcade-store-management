const api = ArcadeApi;
const ui = ArcadeUi;

const state = {
    customers: [],
    categories: [],
    products: [],
    sales: [],
    saleItems: [],
    report: {
        salesCount: 0,
        itemsSold: 0,
        totalRevenue: 0
    }
};

async function loadAll(showSuccess = false) {
    ui.setLoading(true);

    try {
        const data = await api.loadDashboard();
        state.customers = data.customers;
        state.categories = data.categories;
        state.products = data.products;
        state.sales = data.sales;
        state.report = data.report;

        ui.renderDashboard(state);
        ui.updateActionStates(state);
        ui.setConnectionState("ready");

        if (showSuccess) {
            ui.toast("Workspace refreshed.");
        }
    } catch (error) {
        ui.setConnectionState("error");
        ui.toast(error.message, "error");
    } finally {
        ui.setLoading(false);
    }
}

async function submitForm(form, action, successMessage) {
    setFormDisabled(form, true);

    try {
        await action(ui.readForm(form));
        ui.resetForm(form);
        await loadAll();
        ui.toast(successMessage);
    } catch (error) {
        ui.toast(error.message, "error");
    } finally {
        setFormDisabled(form, false);
        ui.updateActionStates(state);
    }
}

function addSaleItem(form) {
    const data = ui.readForm(form);
    const productId = Number(data.productId);
    const quantity = Number(data.quantity);
    const product = state.products.find((currentProduct) => currentProduct.id === productId);

    if (!product || !Number.isInteger(quantity) || quantity <= 0) {
        ui.toast("Select a product and enter a valid quantity.", "error");
        return;
    }

    const existingItem = state.saleItems.find((item) => item.productId === productId);
    const alreadySelected = existingItem?.quantity || 0;
    const availableStock = product.stockQuantity - alreadySelected;

    if (quantity > availableStock) {
        ui.toast(`Only ${availableStock} unit(s) available for ${product.name}.`, "error");
        return;
    }

    if (existingItem) {
        existingItem.quantity += quantity;
    } else {
        state.saleItems.push({ productId, quantity });
    }

    form.quantity.value = "1";
    ui.renderSaleItems(state.saleItems, state.products);
    ui.updateActionStates(state);
    ui.toast("Item added to current sale.");
}

async function finishSale() {
    const customerId = Number(ui.$("#saleItemForm select[name='customerId']").value);

    if (!customerId || state.saleItems.length === 0) {
        ui.toast("Select a customer and add at least one item.", "error");
        return;
    }

    ui.$("#finishSaleButton").disabled = true;

    try {
        await api.registerSale({
            customerId,
            items: state.saleItems
        });
        state.saleItems = [];
        await loadAll();
        ui.toast("Sale completed.");
    } catch (error) {
        ui.toast(error.message, "error");
    } finally {
        ui.updateActionStates(state);
    }
}

async function changeStock(button) {
    const controls = button.closest(".stock-controls");
    const input = controls.querySelector("input");
    const quantity = Number(input.value);
    const productId = Number(button.dataset.productId);
    const action = button.dataset.stockAction;

    if (!Number.isInteger(quantity) || quantity <= 0) {
        ui.toast("Enter a valid stock quantity.", "error");
        return;
    }

    button.disabled = true;

    try {
        await api.changeStock(productId, action, quantity);
        await loadAll();
        ui.toast(action === "in" ? "Stock added." : "Stock removed.");
    } catch (error) {
        ui.toast(error.message, "error");
    } finally {
        button.disabled = false;
    }
}

function removeSaleItem(button) {
    state.saleItems.splice(Number(button.dataset.removeItem), 1);
    ui.renderSaleItems(state.saleItems, state.products);
    ui.updateActionStates(state);
}

function setFormDisabled(form, disabled) {
    form.querySelectorAll("button, input, select").forEach((field) => {
        field.disabled = disabled;
    });
}

ui.$("#refreshButton").addEventListener("click", () => loadAll(true));

ui.$("#customerForm").addEventListener("submit", (event) => {
    event.preventDefault();
    submitForm(event.currentTarget, api.createCustomer, "Customer added.");
});

ui.$("#categoryForm").addEventListener("submit", (event) => {
    event.preventDefault();
    submitForm(event.currentTarget, api.createCategory, "Category added.");
});

ui.$("#productForm").addEventListener("submit", (event) => {
    event.preventDefault();
    submitForm(event.currentTarget, (data) => api.createProduct({
        name: data.name,
        description: data.description,
        price: Number(data.price),
        stockQuantity: Number(data.stockQuantity),
        categoryId: Number(data.categoryId)
    }), "Product added.");
});

ui.$("#saleItemForm").addEventListener("submit", (event) => {
    event.preventDefault();
    addSaleItem(event.currentTarget);
});

ui.$("#saleItems").addEventListener("click", (event) => {
    const button = event.target.closest("[data-remove-item]");
    if (button) {
        removeSaleItem(button);
    }
});

ui.$("#productsGrid").addEventListener("click", (event) => {
    const button = event.target.closest("[data-stock-action]");
    if (button) {
        changeStock(button);
    }
});

ui.$("#finishSaleButton").addEventListener("click", finishSale);

loadAll();
