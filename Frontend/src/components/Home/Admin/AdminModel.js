/**
 * Admin is the model to store admin information for further access in different components.
 */
let admin = {
  admin_name: "",
  get name() {
    return this.admin_name;
  },
  set name(value) {
    this.name = value;
  },
};
