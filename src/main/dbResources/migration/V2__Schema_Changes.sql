-- Start of module_resource foreign key constraints modification
ALTER TABLE module_resource
DROP CONSTRAINT IF EXISTS fk_module_resource_module;

ALTER TABLE module_resource
DROP CONSTRAINT IF EXISTS fk_module_resource_resource;

ALTER TABLE module_resource
ADD CONSTRAINT fk_module_resource_module
FOREIGN KEY (module_id)
REFERENCES module(id)
ON DELETE CASCADE;

ALTER TABLE module_resource
ADD CONSTRAINT fk_module_resource_resource
FOREIGN KEY (resource_id)
REFERENCES resource(id)
ON DELETE CASCADE;
-- End of module_resource foreign key constraints modification