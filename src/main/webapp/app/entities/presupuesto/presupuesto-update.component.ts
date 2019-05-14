import { Component, OnInit } from '@angular/core';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { JhiAlertService } from 'ng-jhipster';
import { IPresupuesto, Presupuesto } from 'app/shared/model/presupuesto.model';
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

    editForm = this.fb.group({
        id: [],
        nombre: [null, [Validators.required]],
        pedido: [],
        proveedor: []
    });

    constructor(
        protected jhiAlertService: JhiAlertService,
        protected presupuestoService: PresupuestoService,
        protected pedidoService: PedidoService,
        protected proveedorService: ProveedorService,
        protected activatedRoute: ActivatedRoute,
        private fb: FormBuilder
    ) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ presupuesto }) => {
            this.updateForm(presupuesto);
            this.presupuesto = presupuesto;
        });
        this.pedidoService
            .query()
            .pipe(
                filter((mayBeOk: HttpResponse<IPedido[]>) => mayBeOk.ok),
                map((response: HttpResponse<IPedido[]>) => response.body)
            )
            .subscribe((res: IPedido[]) => (this.pedidos = res), (res: HttpErrorResponse) => this.onError(res.message));
        this.proveedorService
            .query({ filter: 'presupuesto-is-null' })
            .pipe(
                filter((mayBeOk: HttpResponse<IProveedor[]>) => mayBeOk.ok),
                map((response: HttpResponse<IProveedor[]>) => response.body)
            )
            .subscribe(
                (res: IProveedor[]) => {
                    if (!this.presupuesto.proveedor || !this.presupuesto.proveedor.id) {
                        this.proveedors = res;
                    } else {
                        this.proveedorService
                            .find(this.presupuesto.proveedor.id)
                            .pipe(
                                filter((subResMayBeOk: HttpResponse<IProveedor>) => subResMayBeOk.ok),
                                map((subResponse: HttpResponse<IProveedor>) => subResponse.body)
                            )
                            .subscribe(
                                (subRes: IProveedor) => (this.proveedors = [subRes].concat(res)),
                                (subRes: HttpErrorResponse) => this.onError(subRes.message)
                            );
                    }
                },
                (res: HttpErrorResponse) => this.onError(res.message)
            );
    }

    updateForm(presupuesto: IPresupuesto) {
        this.editForm.patchValue({
            id: presupuesto.id,
            nombre: presupuesto.nombre,
            pedido: presupuesto.pedido,
            proveedor: presupuesto.proveedor
        });
    }

    previousState() {
        window.history.back();
    }

    save() {
        this.isSaving = true;
        const presupuesto = this.createFromForm();
        if (presupuesto.id !== undefined) {
            this.subscribeToSaveResponse(this.presupuestoService.update(presupuesto));
        } else {
            this.subscribeToSaveResponse(this.presupuestoService.create(presupuesto));
        }
    }

    private createFromForm(): IPresupuesto {
        const entity = {
            ...new Presupuesto(),
            id: this.editForm.get(['id']).value,
            nombre: this.editForm.get(['nombre']).value,
            pedido: this.editForm.get(['pedido']).value,
            proveedor: this.editForm.get(['proveedor']).value
        };
        return entity;
    }

    protected subscribeToSaveResponse(result: Observable<HttpResponse<IPresupuesto>>) {
        result.subscribe((res: HttpResponse<IPresupuesto>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
    }

    protected onSaveSuccess() {
        this.isSaving = false;
        this.previousState();
    }

    protected onSaveError() {
        this.isSaving = false;
    }
    protected onError(errorMessage: string) {
        this.jhiAlertService.error(errorMessage, null, null);
    }

    trackPedidoById(index: number, item: IPedido) {
        return item.id;
    }

    trackProveedorById(index: number, item: IProveedor) {
        return item.id;
    }
}
