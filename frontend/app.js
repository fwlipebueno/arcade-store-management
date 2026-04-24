const API_URL = "http://localhost:8080/api";

const state = {
    customers: [],
    categories: [],
    products: [],
    sales: [],
    saleItems: []
};

const money = new Intl.NumberFormat("en-US", {
    style: "currency",
    currency: "BRL"
});

const $ = (selector) => document.querySelector(selector);

async function request(path, options = {}) {
    const response = await fetch(`${API_URL}${path}`, {
        headers: {
            "Content-Type": "application/json",
            ...(options.headers || {})
        },
        ...options
    });

    if (response.status === 204) {
        return null;
    }

    const body = await response.json();

    if (!response.ok) {
        const fields = body.fields ? Object.values(body.fields).join(" ") : "";
        throw new Error([body.message, fields].filter(Boolean).join(" "));
    }

    return body;
}

async function loadAll() {
    const [customers, categories, products, sales, report] = await Promise.all([
        request("/customers"),
        request("/categories"),
        request("/products"),
        request("/sales"),
        request("/sales/report")
    ]);

    state.customers = customers;
    state.categories = categories;
    state.products = products;
    state.sales = sales;

    renderSummary(report);
    renderCustomers();
    renderCategories();
    renderProducts();
    renderSaleControls();
    renderSales();
}

function renderSummary(report) {
    $("#totalSales").textContent = report.salesCount;
    $("#totalItems").textContent = report.itemsSold;
    $("#totalRevenue").textContent = money.format(report.totalRevenue);
}

function renderCustomers() {
    $("#customersList").innerHTML = state.customers.map((customer) => `
        <article class="item">
            <div>
                <strong>${escapeHtml(customer.name)}</strong>
                <span>${escapeHtml(customer.cpf)}</span>
            </div>
            <span>${escapeHtml(customer.email || customer.phone || "-")}</span>
        </article>
    `).join("");
}

function renderCategories() {
    $("#categoriesList").innerHTML = state.categories.map((category) => `
        <span class="chip">${escapeHtml(category.name)}</span>
    `).join("");

    const productCategorySelect = $("#productForm select[name='categoryId']");
    productCategorySelect.innerHTML = state.categories.map((category) => `
        <option value="${category.id}">${escapeHtml(category.name)}</option>
    `).join("");
}

function renderProducts() {
    $("#productsTable").innerHTML = state.products.map((product) => {
        const stockClass = product.stockQuantity <= 3 ? "stock-low" : "stock-ok";
        return `
            <tr>
                <td>
                    <strong>${escapeHtml(product.name)}</strong><br>
                    <span>${escapeHtml(product.description || "-")}</span>
                </td>
                <td>${escapeHtml(product.category.name)}</td>
                <td>${money.format(product.price)}</td>
                <td class="${stockClass}">${product.stockQuantity}</td>
            </tr>
        `;
    }).join("");
}

function renderSaleControls() {
    $("#saleItemForm select[name='customerId']").innerHTML = state.customers.map((customer) => `
        <option value="${customer.id}">${escapeHtml(customer.name)}</option>
    `).join("");

    $("#saleItemForm select[name='productId']").innerHTML = state.products.map((product) => `
        <option value="${product.id}">${escapeHtml(product.name)} - ${money.format(product.price)}</option>
    `).join("");

    renderSaleItems();
}

function renderSaleItems() {
    if (state.saleItems.length === 0) {
        $("#saleItems").innerHTML = "";
        return;
    }

    $("#saleItems").innerHTML = state.saleItems.map((item, index) => {
        const product = state.products.find((currentProduct) => currentProduct.id === item.productId);
        return `
            <article class="item">
                <div>
                    <strong>${escapeHtml(product?.name || "Product")}</strong>
                    <span>Quantity: ${item.quantity}</span>
                </div>
                <button type="button" data-remove-item="${index}">Remove</button>
            </article>
        `;
    }).join("");

    document.querySelectorAll("[data-remove-item]").forEach((button) => {
        button.addEventListener("click", () => {
            state.saleItems.splice(Number(button.dataset.removeItem), 1);
            renderSaleItems();
        });
    });
}

function renderSales() {
    $("#salesList").innerHTML = state.sales.slice().reverse().map((sale) => `
        <article class="item">
            <div>
                <strong>#${sale.id} - ${escapeHtml(sale.customer.name)}</strong>
                <span>${new Date(sale.saleDateTime).toLocaleString("en-US")}</span>
            </div>
            <span>${money.format(sale.total)}</span>
        </article>
    `).join("");
}

function formData(form) {
    return Object.fromEntries(new FormData(form).entries());
}

function resetForm(form) {
    form.reset();
}

function toast(message) {
    const element = $("#toast");
    element.textContent = message;
    element.classList.add("visible");
    window.clearTimeout(toast.timeout);
    toast.timeout = window.setTimeout(() => element.classList.remove("visible"), 3200);
}

function escapeHtml(value) {
    return String(value)
        .replaceAll("&", "&amp;")
        .replaceAll("<", "&lt;")
        .replaceAll(">", "&gt;")
        .replaceAll('"', "&quot;")
        .replaceAll("'", "&#039;");
}

$("#refreshButton").addEventListener("click", () => {
    loadAll().catch((error) => toast(error.message));
});

$("#customerForm").addEventListener("submit", async (event) => {
    event.preventDefault();
    const data = formData(event.currentTarget);

    try {
        await request("/customers", {
            method: "POST",
            body: JSON.stringify(data)
        });
        resetForm(event.currentTarget);
        await loadAll();
        toast("Customer added.");
    } catch (error) {
        toast(error.message);
    }
});

$("#categoryForm").addEventListener("submit", async (event) => {
    event.preventDefault();
    const data = formData(event.currentTarget);

    try {
        await request("/categories", {
            method: "POST",
            body: JSON.stringify(data)
        });
        resetForm(event.currentTarget);
        await loadAll();
        toast("Category added.");
    } catch (error) {
        toast(error.message);
    }
});

$("#productForm").addEventListener("submit", async (event) => {
    event.preventDefault();
    const data = formData(event.currentTarget);

    try {
        await request("/products", {
            method: "POST",
            body: JSON.stringify({
                name: data.name,
                description: data.description,
                price: Number(data.price),
                stockQuantity: Number(data.stockQuantity),
                categoryId: Number(data.categoryId)
            })
        });
        resetForm(event.currentTarget);
        await loadAll();
        toast("Product added.");
    } catch (error) {
        toast(error.message);
    }
});

$("#saleItemForm").addEventListener("submit", (event) => {
    event.preventDefault();
    const data = formData(event.currentTarget);

    state.saleItems.push({
        productId: Number(data.productId),
        quantity: Number(data.quantity)
    });

    event.currentTarget.quantity.value = 1;
    renderSaleItems();
});

$("#finishSaleButton").addEventListener("click", async () => {
    const customerId = Number($("#saleItemForm select[name='customerId']").value);

    try {
        await request("/sales", {
            method: "POST",
            body: JSON.stringify({
                customerId,
                items: state.saleItems
            })
        });
        state.saleItems = [];
        await loadAll();
        toast("Sale completed.");
    } catch (error) {
        toast(error.message);
    }
});

loadAll().catch((error) => toast(error.message));
