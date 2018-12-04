import { IPedido } from 'app/shared/model//pedido.model';
import { IProveedor } from 'app/shared/model//proveedor.model';

export interface IPresupuesto {
    id?: number;
    nombre?: string;
    pedido?: IPedido;
    proveedor?: IProveedor;
}

export class Presupuesto implements IPresupuesto {
    constructor(public id?: number, public nombre?: string, public pedido?: IPedido, public proveedor?: IProveedor) {}
}
