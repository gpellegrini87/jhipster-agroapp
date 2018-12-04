import { ICliente } from 'app/shared/model//cliente.model';
import { IPresupuesto } from 'app/shared/model//presupuesto.model';

export interface IPedido {
    id?: number;
    nombre?: string;
    descripcion?: string;
    cliente?: ICliente;
    presupuestos?: IPresupuesto[];
}

export class Pedido implements IPedido {
    constructor(
        public id?: number,
        public nombre?: string,
        public descripcion?: string,
        public cliente?: ICliente,
        public presupuestos?: IPresupuesto[]
    ) {}
}
