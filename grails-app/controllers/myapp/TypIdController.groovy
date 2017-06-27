package myapp

import static org.springframework.http.HttpStatus.*
import grails.transaction.Transactional

@Transactional(readOnly = true)
class TypIdController {

    static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE"]

    def index(Integer max) {
        params.max = Math.min(max ?: 10, 100)
        respond TypId.list(params), model:[typIdCount: TypId.count()]
    }

    def show(TypId typId) {
        respond typId
    }

    def create() {
        respond new TypId(params)
    }

    @Transactional
    def save(TypId typId) {
        if (typId == null) {
            transactionStatus.setRollbackOnly()
            notFound()
            return
        }

        if (typId.hasErrors()) {
            transactionStatus.setRollbackOnly()
            respond typId.errors, view:'create'
            return
        }

        typId.save flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.created.message', args: [message(code: 'typId.label', default: 'TypId'), typId.id])
                redirect typId
            }
            '*' { respond typId, [status: CREATED] }
        }
    }

    def edit(TypId typId) {
        respond typId
    }

    @Transactional
    def update(TypId typId) {
        if (typId == null) {
            transactionStatus.setRollbackOnly()
            notFound()
            return
        }

        if (typId.hasErrors()) {
            transactionStatus.setRollbackOnly()
            respond typId.errors, view:'edit'
            return
        }

        typId.save flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.updated.message', args: [message(code: 'typId.label', default: 'TypId'), typId.id])
                redirect typId
            }
            '*'{ respond typId, [status: OK] }
        }
    }

    @Transactional
    def delete(TypId typId) {

        if (typId == null) {
            transactionStatus.setRollbackOnly()
            notFound()
            return
        }

        typId.delete flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.deleted.message', args: [message(code: 'typId.label', default: 'TypId'), typId.id])
                redirect action:"index", method:"GET"
            }
            '*'{ render status: NO_CONTENT }
        }
    }

    protected void notFound() {
        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.not.found.message', args: [message(code: 'typId.label', default: 'TypId'), params.id])
                redirect action: "index", method: "GET"
            }
            '*'{ render status: NOT_FOUND }
        }
    }
}
