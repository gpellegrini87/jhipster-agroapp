import { IPresupuesto } from 'app/shared/model/presupuesto.model';
import { ICliente } from 'app/shared/model/cliente.model';

export interface IPedido {
    id?: number;
    nombre?: string;
    descripcion?: string;
    presupuestos?: IPresupuesto[];
    cliente?: ICliente;
}

export class Pedido implements IPedido {
    constructor(
        public id?: number,
        public nombre?: string,
        public descripcion?: string,
        public presupuestos?: IPresupuesto[],
        public cliente?: ICliente
    ) {}
}
