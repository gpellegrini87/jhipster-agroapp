export interface ICliente {
    id?: number;
    nombre?: string;
}

export class Cliente implements ICliente {
    constructor(public id?: number, public nombre?: string) {}
}
