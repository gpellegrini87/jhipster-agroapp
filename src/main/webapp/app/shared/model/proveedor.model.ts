export interface IProveedor {
    id?: number;
    nombre?: string;
}

export class Proveedor implements IProveedor {
    constructor(public id?: number, public nombre?: string) {}
}
