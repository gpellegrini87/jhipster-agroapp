import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { JhiAlertService } from 'ng-jhipster';

import { IPresupuesto } from 'app/shared/model/presupuesto.model';
import { PresupuestoService } from './presupuesto.service';
import { IPedido } from 'app/shared/model/pedido.model';
import { PedidoService } from 'app/entities/pedido';
import { IProveedor } from 'app/shared/model/proveedor.model';
import { ProveedorService } from 'app/entities/proveedor';

@Component({
    selector: 'jhi-presupuesto-update',
    templateUrl: './presupuesto-update.component.html'
})
export class PresupuestoUpdateComponent implements OnInit {
    presupuesto: IPresupuesto;
    isSaving: boolean;

    pedidos: IPedido[];

    proveedors: IProveedor[];

    constructor(
        private jhiAlertService: JhiAlertService,
        private presupuestoService: PresupuestoService,
        private pedidoService: PedidoService,
        private proveedorService: ProveedorService,
        private activatedRoute: ActivatedRoute
    ) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ presupuesto }) => {
            this.presupuesto = presupuesto;
        });
        this.pedidoService.query().subscribe(
            (res: HttpResponse<IPedido[]>) => {
                this.pedidos = res.body;
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
        this.proveedorService.query({ filter: 'presupuesto-is-null' }).subscribe(
            (res: HttpResponse<IProveedor[]>) => {
                if (!this.presupuesto.proveedor || !this.presupuesto.proveedor.id) {
                    this.proveedors = res.body;
                } else {
                    this.proveedorService.find(this.presupuesto.proveedor.id).subscribe(
                        (subRes: HttpResponse<IProveedor>) => {
                            this.proveedors = [subRes.body].concat(res.body);
                        },
                        (subRes: HttpErrorResponse) => this.onError(subRes.message)
                    );
                }
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
    }

    previousState() {
        window.history.back();
    }

    save() {
        this.isSaving = true;
        if (this.presupuesto.id !== undefined) {
            this.subscribeToSaveResponse(this.presupuestoService.update(this.presupuesto));
        } else {
            this.subscribeToSaveResponse(this.presupuestoService.create(this.presupuesto));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<IPresupuesto>>) {
        result.subscribe((res: HttpResponse<IPresupuesto>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
    }

    private onSaveSuccess() {
        this.isSaving = false;
        this.previousState();
    }

    private onSaveError() {
        this.isSaving = false;
    }

    private onError(errorMessage: string) {
        this.jhiAlertService.error(errorMessage, null, null);
    }

    trackPedidoById(index: number, item: IPedido) {
        return item.id;
    }

    trackProveedorById(index: number, item: IProveedor) {
        return item.id;
    }
}
